const express = require('express');
const app = express();
const server = require('http').createServer(app);
const io = require('socket.io')(server);
const webrtc = require('wrtc');


const ACTIONS = require("./actions");
const {api} = require("./api");
const {use} = require("express/lib/router");
const PORT = process.env.PORT || 3001

// let conferenceRoom = {
//     id: 0,
//     users: [
//         {
//             id: 0,
//             socket: null,
//             inputPeerConnection: null,
//             outputPeerConnection: null,
//             stream: []
//         }
//     ]
// }
let conferenceRooms = [];

const getPeerConnectionConfiguration = () => {
    // let stunServers = api.getStunServers();
    // let urls = stunServers.map(stunServer => {
    //     return {"urls": stunServer};
    // });
    // return {"iceServers": [...urls]};
    // return {'iceServers': [{'urls': 'stun:stun.services.mozilla.com'}, {'urls': 'stun:stun.l.google.com:19302'}]};
    return {'iceServers': [{'urls': 'stun:stun.l.google.com:19302'}]};
};

const getOrCreateConferenceRoom = (roomId) => {
    let foundRoom = conferenceRooms.find(room => room.id === roomId);
    if (!foundRoom) {
        foundRoom = {
            id: roomId,
            users: []
        };
        conferenceRooms.push(foundRoom);
    }
    return foundRoom;
}

const addClientsTracks = (peerConnection, room, userId) => {
    for (let user of room.users) {
        if (user.id === userId) {
            continue;
        }

        user.stream.getTracks().forEach(s => {
            peerConnection.addTrack(s, user.stream);
        });
    }
}

const notifyRoomMembers = (room) => {
    room.users.forEach(user => {
        let oldPeerConnection = createOutputConnection(user, room);
        if (oldPeerConnection) {
            oldPeerConnection.close();
        }
    });
}

const createOutputConnection = (user, room) => {
    let socket = user.socket;
    if (!socket) {
        return console.warn(`User ${user.id} is not connected.`);
    }
    let outputPeerConnection = new webrtc.RTCPeerConnection(getPeerConnectionConfiguration());
    let oldOutputPeerConnection = user.outputPeerConnection;
    user.outputPeerConnection = outputPeerConnection;
    addClientsTracks(outputPeerConnection, room, user.id);
    outputPeerConnection.onicecandidate = e => {
        if (e.candidate) {
            socket.emit(ACTIONS.GET_REMOTES__ICE_CANDIDATE, e.candidate);
        }
    }

    outputPeerConnection.onnegotiationneeded = async () => {
        const offer = await outputPeerConnection.createOffer();
        await outputPeerConnection.setLocalDescription(offer);
        socket.emit(ACTIONS.GET_REMOTES__JOIN, offer);
    }
    return oldOutputPeerConnection;
}

const handleInputTrack = (peerConnection, room, socket) => {
    let tracksCount = 0;
    peerConnection.ontrack = e => {
        tracksCount++;
        if (tracksCount === 2) {
            tracksCount = 0;
            let user = room.users.find(user => user.id === socket.id);
            if (user) {
                user.stream = e.streams[0];
                notifyRoomMembers(room);
            }
        }
    }
}

const handleJoinRoomAction = async (sdp, roomId, userId, socket) => {
    socket.id = userId;
    if (io.sockets.adapter.rooms.get(roomId)?.has(userId)) {
        return console.warn(`User ${userId} is already connected to room ${roomId}`);
    }
    socket.join(roomId);
    let room = getOrCreateConferenceRoom(roomId);
    let peerConnection = new webrtc.RTCPeerConnection(getPeerConnectionConfiguration());
    if (!room.users.find(user => user.id === socket.id)) {
        room.users.push({id: socket.id, socket:socket, inputPeerConnection: peerConnection, stream: null});
    }
    handleInputTrack(peerConnection, room, socket);

    peerConnection.onicecandidate = e => {
        if (e.candidate) {
            socket.emit(ACTIONS.SEND_LOCAL__ICE_CANDIDATE, e.candidate);
        }
    }
    const remoteDescription = new webrtc.RTCSessionDescription(sdp);
    await peerConnection.setRemoteDescription(remoteDescription);
    let answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);
    socket.emit(ACTIONS.SEND_LOCAL__JOINED, peerConnection.localDescription);
}

const leaveRooms = (socket) => {
    socket.rooms.forEach(room => {
        socket.leave(room);
        let foundRoomIndex = conferenceRooms.findIndex(r => r.id === room);
        if (foundRoomIndex < 0) {
            return;
        }
        let foundRoom = conferenceRooms[foundRoomIndex];
        if (foundRoom) {
            let index = foundRoom.users.findIndex(u => u.id === socket.id);
            foundRoom.users.splice(index, 1);
        }
        if (foundRoom.users.length === 0) {
            conferenceRooms.splice(foundRoomIndex, 1);
        }
    });
};

io.on("connection", socket => {
    socket.on(ACTIONS.SEND_LOCAL__JOIN_ROOM, async (sdp, roomId, userId) => {
        await handleJoinRoomAction(sdp, roomId, userId, socket);
    });

    socket.on(ACTIONS.SEND_LOCAL__ICE_CANDIDATE, (roomId, candidate) => {
        let room = conferenceRooms.find(room => room.id === roomId);
        if (!room) {
            return;
        }
        let peer = room.users
            .find(user => user.id === socket.id)
            .inputPeerConnection;
        peer.addIceCandidate(new webrtc.RTCIceCandidate(candidate));
    });

    socket.on(ACTIONS.GET_REMOTES__ICE_CANDIDATE, (roomId, candidate) => {
        let peer = conferenceRooms.find(room => room.id === roomId).users
            .find(user => user.id === socket.id)
            .outputPeerConnection;
        peer.addIceCandidate(new webrtc.RTCIceCandidate(candidate));
    })

    socket.on(ACTIONS.GET_REMOTES__JOINED, (roomId, sdp) => {
        let user = conferenceRooms.find(room => room.id === roomId).users.find(user => user.id === socket.id);
        user.outputPeerConnection.setRemoteDescription(new webrtc.RTCSessionDescription(sdp));
    })

    socket.on("disconnecting", () => {
        leaveRooms(socket);
    });
    socket.on(ACTIONS.SEND_LOCAL__LEAVE_ROOM, () => {
        leaveRooms(socket);
    });
})

// Endpoint to check all active rooms with their members
app.get("/rooms", (request, response) => {
    let rooms = [];
    for (let room of conferenceRooms) {
        let roomDto = {
            id: room.id,
            users: null
        }
        roomDto.users = room.users.map(user => ({
            id: user.id,
        }));
        rooms.push(roomDto);
    }
    let responseBody = {
        conferenceRooms: rooms,
    };
    response.json(responseBody);
});

server.listen(PORT, () => {
    console.log('Server started');
    if (conferenceRooms.length !== 0) {
        conferenceRooms = [];
    }
})
