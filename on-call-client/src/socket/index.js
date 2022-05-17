import {io} from "socket.io-client";

const VIDEOCONFERENCE_SERVER_URL = "http://localhost:3001/";

const options = {
    "force new connection": true,
    reconnectionAttempts: "Infinity",
    timeout: 10000,
    transports: ["websocket"]
}

const socket = io(VIDEOCONFERENCE_SERVER_URL, options);

export default socket;