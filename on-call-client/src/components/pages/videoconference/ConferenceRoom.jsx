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

const getPeerConnectionConfiguration = (stunServers) => {
    // let urls = stunServers.map(stunServer => {
    //     return {"urls": stunServer};
    // });
    // return {"iceServers": [...urls]};
    return {'iceServers': [{'urls': 'stun:stun.services.mozilla.com'}, {'urls': 'stun:stun.l.google.com:19302'}]}
}

class ConferenceRoom extends React.Component {
    // state = {
    //     localStream: null,
    //     videos: []
    // }

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
        // this.setState({localStream: stream});
        this.localMediaStream.current = stream;
    }

    addCompanionMediaStream(stream) {
        this.companionMediaStreams.current.push(stream);
        this.setState({clientsCount: this.state.clientsCount + 1});
    }

    async connect() {
        let stream = await navigator.mediaDevices.getUserMedia(MEDIA_CONSTRAINTS);
        this.setLocalStream(stream);
        let peer = new RTCPeerConnection({'iceServers': [{ 'urls': 'stun:stun.services.mozilla.com' }]});
        stream.getTracks().forEach(track => {
            peer.addTrack(track, stream);
        });
        socket.on(SOCKET_ACTIONS.ICE_CANDIDATE, candidate => {
            peer.addIceCandidate(new RTCIceCandidate(candidate));
        });
        peer.onicecandidate = e => {
            if (e.candidate && e.candidate.candidate) {
                socket.emit(SOCKET_ACTIONS.ICE_CANDIDATE, this.props.roomId, e.candidate);
            }
        }
        let tracksCount = 0;
        peer.ontrack = e => {
            tracksCount++;
            if (tracksCount === 2) {
                tracksCount = 0;
                this.addCompanionMediaStream(e.streams[0]);
            }
        }
        socket.on(SOCKET_ACTIONS.JOINED, sdp => {
            peer.setRemoteDescription(new RTCSessionDescription(sdp));
            this.setState({clientsCount: this.state.clientsCount + 1});
        })
        peer.onnegotiationneeded = async () => {
            const offer = await peer.createOffer();
            await peer.setLocalDescription(offer);
            socket.emit(SOCKET_ACTIONS.JOIN_ROOM, offer, this.props.roomId, this.props.authorizedUser.id);
        }
    }

    async componentDidMount() {
        if (socket && this.props.isAuthorized) {
            await this.connect();
        }
    }

    async componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.authorizedUser !== this.props.authorizedUser && socket && this.props.isAuthorized) {
            await this.connect();
        }
    }

    componentWillUnmount() {
        this.localMediaStream.current.getTracks().forEach(track => track.stop());
        socket.emit(SOCKET_ACTIONS.LEAVE_ROOM);
        socket.off(SOCKET_ACTIONS.JOINED);
        socket.off(SOCKET_ACTIONS.ICE_CANDIDATE);
        socket.off(SOCKET_ACTIONS.JOINED);
        socket.off(SOCKET_ACTIONS.JOINED);
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