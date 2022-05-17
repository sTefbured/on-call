const express = require('express');
const app = express();
const server = require('http').createServer(app);
const io = require('socket.io')(server);
const webrtc = require('wrtc');


const ACTIONS = require("./actions");
const {api} = require("./api");
const PORT = process.env.PORT || 3001

// let conferenceRoom = {
//     id: 0,
//     userStreams: [
//         {
//             id: 0,
//             inputPeerConnection: null,
//             outputPeerConnection: null,
//             streams: []
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
    return {'iceServers': [{'urls': 'stun:stun.services.mozilla.com'}, {'urls': 'stun:stun.l.google.com:19302'}]};
};

const getConferenceRoom = (roomId) => {
    let foundRoom = conferenceRooms.find(room => room.id === roomId);
    if (!foundRoom) {
        foundRoom = {
            id: roomId,
            userStreams: []
        };
        conferenceRooms.push(foundRoom);
    }
    return foundRoom;
}

const addClientsTracks = (peerConnection, room, userId) => {
    for (let user of room.userStreams) {
        if (user.id === userId) {
            continue;
        }

        user.streams.getTracks().forEach(s => {
            peerConnection.addTrack(s, user.streams);
        });
    }
}

const handleJoinRoomAction = async (sdp, roomId, userId, socket) => {
    socket.id = userId;
    if (Array.of(socket.rooms).includes(roomId)) {
        return console.warn(`User ${socket.id} is already connected to room ${roomId}`);
    }
    let room = getConferenceRoom(roomId);
    let peerConnection = new webrtc.RTCPeerConnection({'iceServers': [{'urls': 'stun:stun.services.mozilla.com'}]});
    addClientsTracks(peerConnection, room, socket.id);
    if (!room.userStreams.find(user => user.id === socket.id)) {
        room.userStreams.push({id: socket.id, peer: peerConnection, streams: null});
    }
    let tracksCount = 0;
    peerConnection.ontrack = e => {
        tracksCount++;
        if (tracksCount === 2) {
            tracksCount = 0;
            let user = room.userStreams.find(user => user.id === socket.id);
            if (user) {
                user.streams = e.streams[0];
            }
            for (let companionUser of room.userStreams) {
                if (companionUser.id === user.id) {
                    continue;
                }
                user.streams.getTracks().forEach(s => {
                    companionUser.peer.addTrack(s, user.streams);
                });
            }
        }
        //TODO: send current to existing clients
    }
    peerConnection.onicecandidate = e => {
        if (e.candidate) {
            socket.emit(ACTIONS.ICE_CANDIDATE, e.candidate);
        }
    }
    const remoteDescription = new webrtc.RTCSessionDescription(sdp);
    await peerConnection.setRemoteDescription(remoteDescription);
    let answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);
    socket.join(roomId);
    socket.emit(ACTIONS.JOINED, peerConnection.localDescription);
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
            let index = foundRoom.userStreams.findIndex(u => u.id === socket.id);
            foundRoom.userStreams.splice(index, 1);
        }
        if (foundRoom.userStreams.length === 0) {
            conferenceRooms.splice(foundRoomIndex, 1);
        }
    });
};

io.on("connection", socket => {
    socket.on(ACTIONS.JOIN_ROOM, async (sdp, roomId, userId) => {
        await handleJoinRoomAction(sdp, roomId, userId, socket);
    });

    socket.on(ACTIONS.ICE_CANDIDATE, (roomId, candidate) => {
        let peer = conferenceRooms.find(room => room.id === roomId).userStreams
            .find(user => user.id === socket.id)
            .peer;
        peer.addIceCandidate(new webrtc.RTCIceCandidate(candidate));
    });

    socket.on("disconnecting", () => {
        leaveRooms(socket);
    });
    socket.on(ACTIONS.LEAVE_ROOM, () => {
        leaveRooms(socket);
    });
})

// Endpoint to check all active rooms with their members
app.get("/rooms", (request, response) => {
    let rooms = [];
    for (let room of conferenceRooms) {
        let roomDto = {
            id: room.id,
            userStreams: null
        }
        roomDto.userStreams = room.userStreams.map(user => ({
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
