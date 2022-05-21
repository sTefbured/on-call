import React from "react";
import createSocketIoConnection from "../../../socket";
import {SOCKET_ACTIONS} from "../../../socket/socketActions";
import Video from "./video/Video";

const MEDIA_CONSTRAINTS = {
    audio: true,
    video: {
        width: 240
    }
};

const getPeerConnectionConfiguration = () => {
    // let urls = stunServers.map(stunServer => {
    //     return {"urls": stunServer};
    // });
    // return {"iceServers": [...urls]};
    // return {'iceServers': [{'urls': 'stun:stun.services.mozilla.com'}, {'urls': 'stun:stun.l.google.com:19302'}]}
    return {'iceServers': [{ 'urls': 'stun:stun.l.google.com:19302' }]};
}

let socket = null;

class ConferenceRoom extends React.Component {
    state = {
        clientsCount: 0
    }

    constructor(props) {
        super(props);
        this.localMediaStream = React.createRef();
        this.companionMediaStreams = React.createRef();
        this.companionMediaStreams.current = new Map();
        this.companionPeers = React.createRef();
        this.companionPeers.current = new Map();
    }

    setLocalStream(stream) {
        this.localMediaStream.current = stream;
    }

    addCompanionMediaStream(userId, stream) {
        this.companionMediaStreams.current.set(userId, stream);
        this.setState({clientsCount: this.state.clientsCount + 1});
    }

    removeCompanionMediaStream(userId) {
        this.companionMediaStreams.current.get(userId).getTracks().forEach(track => track.stop());
        this.companionMediaStreams.current.delete(userId);
        this.setState({clientsCount: this.state.clientsCount - 1});
    }

    addCompanionPeer(userId, peer) {
        this.companionPeers.current.set(userId, peer);
    }

    removeCompanionPeer(userId) {
        this.companionPeers.current.delete(userId);
    }

    async createOutputConnection() {
        let stream = await navigator.mediaDevices.getUserMedia(MEDIA_CONSTRAINTS);
        this.setLocalStream(stream);
        let peer = new RTCPeerConnection(getPeerConnectionConfiguration());
        stream.getTracks().forEach(track => {
            peer.addTrack(track, stream);
        });
        socket.on(SOCKET_ACTIONS.SEND_LOCAL__ICE_CANDIDATE, candidate => {
            peer.addIceCandidate(new RTCIceCandidate(candidate));
        });
        peer.onicecandidate = e => {
            if (e.candidate && e.candidate.candidate) {
                socket.emit(SOCKET_ACTIONS.SEND_LOCAL__ICE_CANDIDATE, this.props.roomId, e.candidate);
            }
        }
        socket.on(SOCKET_ACTIONS.SEND_LOCAL__JOINED, sdp => {
            peer.setRemoteDescription(new RTCSessionDescription(sdp));
            this.setState({clientsCount: this.state.clientsCount + 1});
        })
        peer.onnegotiationneeded = async () => {
            const offer = await peer.createOffer();
            await peer.setLocalDescription(offer);
            socket.emit(SOCKET_ACTIONS.SEND_LOCAL__JOIN_ROOM, offer, this.props.roomId, this.props.authorizedUser.id);
        }
        await this.addInputHandlers(); //TODO: maybe move upper
    }

    async addInputHandlers() {
        socket.on(SOCKET_ACTIONS.GET_REMOTES__JOIN, async (sdp, userId) => {
            let peerConnection = new RTCPeerConnection(getPeerConnectionConfiguration());
            this.addCompanionPeer(userId, peerConnection);
            let tracksCount = 0;
            peerConnection.ontrack = e => {
                tracksCount++;
                if (tracksCount === 2) {
                    tracksCount = 0;
                    this.addCompanionMediaStream(userId, e.streams[0]);
                }
            }
            socket.on(SOCKET_ACTIONS.GET_REMOTES__ICE_CANDIDATE, (userId, candidate) => {
                if (candidate && candidate.candidate) {
                    this.companionPeers.current.get(userId).addIceCandidate(new RTCIceCandidate(candidate));
                }
            });
            peerConnection.onicecandidate = e => {
                if (e.candidate && e.candidate.candidate) {
                    socket.emit(SOCKET_ACTIONS.GET_REMOTES__ICE_CANDIDATE, this.props.roomId, userId, e.candidate);
                }
            }
            const remoteDescription = new RTCSessionDescription(sdp);
            await peerConnection.setRemoteDescription(remoteDescription);
            let answer = await peerConnection.createAnswer();
            await peerConnection.setLocalDescription(answer);
            socket.emit(SOCKET_ACTIONS.GET_REMOTES__JOINED, this.props.roomId, userId, peerConnection.localDescription);
        });
        socket.on(SOCKET_ACTIONS.GET_REMOTES__USER_LEFT, userId => {
            this.removeCompanionPeer(userId);
            this.removeCompanionMediaStream(userId);
        });
    }

    async componentDidMount() {
        socket = createSocketIoConnection();
        if (socket && this.props.isAuthorized) {
            await this.createOutputConnection();
        }
    }

    async componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.authorizedUser !== this.props.authorizedUser && socket && this.props.isAuthorized) {
            await this.createOutputConnection();
        }
    }

    async componentWillUnmount() {
        this.localMediaStream.current.getTracks().forEach(track => track.stop());
        if (socket) {
            await socket.emit(SOCKET_ACTIONS.SEND_LOCAL__LEAVE_ROOM);
            socket.removeAllListeners();
        }
    }

    render() {
        let videoElements = [];
        this.companionMediaStreams.current.forEach((stream, userId) => {
            videoElements.push(<Video key={userId} videoStream={stream}/>);
        });
        return (
            <div>
                <div>{this.state.clientsCount}</div>
                <Video id={'my-video'} videoStream={this.localMediaStream.current} isMuted={true}/>
                {videoElements}
            </div>
        );
    }
}

export default ConferenceRoom;