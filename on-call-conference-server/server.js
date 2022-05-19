const express = require('express');
const app = express();
const server = require('http').createServer(app);
const io = require('socket.io')(server);
const webrtc = require('wrtc');

const ACTIONS = require("./actions");
const PORT = process.env.PORT || 3001
const PEER_CONNECTION_CONFIGURATION = {'iceServers': [{'urls': 'stun:stun.l.google.com:19302'}]};

// let conferenceRoom = {
//     id: 0,
//     users: [
//         {
//             id: 0,
//             socket: null,
//             inputPeerConnection: null,
//             outputPeerConnections: {},
//             stream: []
//         }
//     ]
// }
let conferenceRooms = [];

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

const createOutputConnectionOfAllCompanions = (user, room) => {
    for (let u of room.users) {
        if (u.id === user.id) {
            continue;
        }
        createOutputConnection(user, room, u);
    }
}

const notifyRoomMembers = (room, newUser) => {
    room.users.forEach(user => {
        if (user.id === newUser.id) {
            return;
        }
        createOutputConnection(user, room, newUser)
    });
}

const createOutputConnection = (destinationUser, room, sourceUser) => {
    let socket = destinationUser.socket;
    if (!socket) {
        return console.warn(`User ${destinationUser.id} is not connected.`);
    }
    let outputPeerConnection = new webrtc.RTCPeerConnection(PEER_CONNECTION_CONFIGURATION);
    destinationUser.outputPeerConnections.set(sourceUser.id, outputPeerConnection);
    sourceUser.stream.getTracks().forEach(track => outputPeerConnection.addTrack(track, sourceUser.stream));
    outputPeerConnection.onicecandidate = e => {
        if (e.candidate) {
            socket.emit(ACTIONS.GET_REMOTES__ICE_CANDIDATE, sourceUser.id, e.candidate);
        }
    }

    outputPeerConnection.onnegotiationneeded = async () => {
        const offer = await outputPeerConnection.createOffer();
        await outputPeerConnection.setLocalDescription(offer);
        socket.emit(ACTIONS.GET_REMOTES__JOIN, offer, sourceUser.id);
    }
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
                notifyRoomMembers(room, user);
                createOutputConnectionOfAllCompanions(user, room);
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
    let peerConnection = new webrtc.RTCPeerConnection(PEER_CONNECTION_CONFIGURATION);
    if (!room.users.find(user => user.id === socket.id)) {
        room.users.push({
            id: socket.id,
            socket:socket,
            inputPeerConnection: peerConnection,
            outputPeerConnections: new Map(),
            stream: null
        });
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
            let userToRemove = foundRoom.users[index];
            foundRoom.users.forEach(u => {
                if (u.id === userToRemove.id) {
                    return;
                }
                u.outputPeerConnections.get(userToRemove.id).close();
                u.socket.emit(ACTIONS.GET_REMOTES__USER_LEFT, socket.id);
            })
            userToRemove.inputPeerConnection.close();
            for (let peerConnection of userToRemove.outputPeerConnections.values()) {
                peerConnection.close();
            }
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

    socket.on(ACTIONS.GET_REMOTES__ICE_CANDIDATE, (roomId, userId, candidate) => {
        let peer = conferenceRooms.find(room => room.id === roomId).users
            .find(user => user.id === socket.id)
            .outputPeerConnections.get(userId);
        peer.addIceCandidate(new webrtc.RTCIceCandidate(candidate));
    })

    socket.on(ACTIONS.GET_REMOTES__JOINED, (roomId, userId, sdp) => {
        let user = conferenceRooms.find(room => room.id === roomId).users.find(user => user.id === socket.id);
        user.outputPeerConnections.get(userId).setRemoteDescription(new webrtc.RTCSessionDescription(sdp));
    })

    socket.on("disconnecting", () => {
        leaveRooms(socket);
    });
    socket.on(ACTIONS.SEND_LOCAL__LEAVE_ROOM, () => {
        leaveRooms(socket);
    });
})

// Endpoint to check all active rooms with their members
app.get("/", (request, response) => {
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
