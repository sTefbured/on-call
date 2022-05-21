import {connect} from "react-redux";
import Chat from "./Chat";
import {
    addMessages,
    clearMessages,
    loadMessages,
    requestChatInfo,
    setCurrentPage
} from "../../../redux/reducers/chatReducer";
import {useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import SockJs from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

const WEBSOCKET_ADDRESS = "http://localhost:8080/ws";

let stompClient = null;

const ChatContainer = (props) => {
    let [isWebsocketConnected, setIsWebsocketConnected] = useState(false);
    let [messageText, setMessageText] = useState("");
    let {id: chatId} = useParams();
    useEffect(() => {
        if (props.isAuthorized) {
            props.requestChatInfo(chatId);
        }
        return () => {
            props.clearMessages();
        }
    }, [props.isAuthorized]);

    useEffect(() => {
        if (props.isAuthorized && !isWebsocketConnected) {
            createWebsocketConnection();
        }
        return () => {
            disconnectFromWebsocket();
        }
    }, [props.chat]);

    const createWebsocketConnection = () => {
        stompClient = Stomp.over(() => {
            return new SockJs(WEBSOCKET_ADDRESS);
        });
        stompClient.debug = () => {};
        stompClient.connect({}, () => {
            setIsWebsocketConnected(true);
            stompClient.subscribe(`/user/${props.authorizedUser.id}/message`, message => {
                let messageObject = JSON.parse(message.body);
                if (messageObject.fieldName) {
                    console.warn(messageObject.message);
                    return;
                }
                if (messageObject.sender.id === props.authorizedUser.id) {
                    setMessageText("");
                }
                props.addMessages([messageObject], true);
            });
        })
    }

    const sendMessage = () => {
        stompClient.send("/app/message", {}, JSON.stringify({
            text: messageText,
            sender: {
                id: props.authorizedUser.id //FIXME: bad, server should do that
            },
            chat: {
                id: chatId
            }
        }));
    }

    const disconnectFromWebsocket = () => {
        if (stompClient) {
            stompClient.disconnect();
        }
    }

    const onInputKeyPress = (event) => {
        if (event.key === "Enter") {
            sendMessage();
        }
    }

    if (!props.chat) {
        return (
            <div>Loading...</div>
        );
    }
    return (
        <Chat {...props}
              messageText={messageText}
              setMessageText={text => setMessageText(text)}
              onInputKeyPress={(e) => onInputKeyPress(e)}/>
    );
}

let mapStateToProps = (state) => ({
    chat: state.chat.chat,
    messages: state.chat.messages,
    currentPage: state.chat.currentPage,
    pageSize: state.chat.defaultPageSize,
    authorizedUser: state.auth.user,
    isAuthorized: state.auth.isAuthorized
});

export default connect(mapStateToProps, {
    requestChatInfo,
    loadMessages,
    clearMessages,
    setCurrentPage,
    addMessages
})(ChatContainer);