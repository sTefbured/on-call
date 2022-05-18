import React from "react";
import socket from "../../../socket";
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

class ConferenceRoom extends React.Component {
    state = {
        clientsCount: 0
    }

    constructor(props) {
        super(props);
        this.localMediaStream = React.createRef();
        this.companionMediaStreams = React.createRef();
        this.companionMediaStreams.current = [];
    }

    setLocalStream(stream) {
        this.localMediaStream.current = stream;
    }

    addCompanionMediaStream(stream) {
        this.companionMediaStreams.current.push(stream);
        this.setState({clientsCount: this.state.clientsCount + 1});
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
        socket.on(SOCKET_ACTIONS.GET_REMOTES__JOIN, async sdp => {
            let peerConnection = new RTCPeerConnection(getPeerConnectionConfiguration());
            let tracksCount = 0;
            peerConnection.ontrack = e => {
                tracksCount++;
                if (tracksCount === 2) {
                    this.addCompanionMediaStream(e.streams[(tracksCount / 2) - 1]);
                }
            }
            socket.on(SOCKET_ACTIONS.GET_REMOTES__ICE_CANDIDATE, candidate => {
                peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
            });
            peerConnection.onicecandidate = e => {
                if (e.candidate && e.candidate.candidate) {
                    socket.emit(SOCKET_ACTIONS.GET_REMOTES__ICE_CANDIDATE, this.props.roomId, e.candidate);
                }
            }
            const remoteDescription = new RTCSessionDescription(sdp);
            await peerConnection.setRemoteDescription(remoteDescription);
            let answer = await peerConnection.createAnswer();
            await peerConnection.setLocalDescription(answer);
            socket.emit(SOCKET_ACTIONS.GET_REMOTES__JOINED, this.props.roomId, peerConnection.localDescription);
        });
    }

    async componentDidMount() {
        if (socket && this.props.isAuthorized) {
            await this.createOutputConnection();
        }
    }

    async componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.authorizedUser !== this.props.authorizedUser && socket && this.props.isAuthorized) {
            await this.createOutputConnection();
        }
    }

    componentWillUnmount() {
        this.localMediaStream.current.getTracks().forEach(track => track.stop());
        socket.emit(SOCKET_ACTIONS.SEND_LOCAL__LEAVE_ROOM);
        socket.off(SOCKET_ACTIONS.SEND_LOCAL__JOINED);
        socket.off(SOCKET_ACTIONS.SEND_LOCAL__ICE_CANDIDATE);
    }

    render() {
        let videoElements = this.companionMediaStreams.current.map(stream => {
            return <Video key={stream.id} videoStream={stream}/>
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