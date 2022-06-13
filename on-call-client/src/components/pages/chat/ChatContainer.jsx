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
import {initializeStompClient} from "../../../redux/reducers/stompReducer";

const ChatContainer = ({isAuthorized, messages, authorizedUser, currentPage, pageSize, chat, requestChatInfo, addMessages,
                           clearMessages, chatId, stompClient, initializeStompClient, setCurrentPage, loadMessages}) => {
    let [messageText, setMessageText] = useState("");
    let {id} = useParams();
    if (!chatId) {
        chatId = id;
    }
    useEffect(() => {
        if (isAuthorized) {
            requestChatInfo(chatId);
        }
        return () => {
            clearMessages();
        }
    }, [isAuthorized]);

    useEffect(() => {
        return stompClient
            ? configureStomp()
            : initializeStompClient();
    }, [stompClient]);

    const configureStomp = () => {
        let subscription = stompClient.subscribe(`/user/${authorizedUser.id}/message`, message => {
            let messageObject = JSON.parse(message.body);
            if (messageObject.fieldName) {
                console.warn(messageObject.message);
                return;
            }
            if (messageObject.sender.id === authorizedUser.id) {
                setMessageText("");
            }
            addMessages([messageObject], true);
        });
        return () => {
            subscription.unsubscribe();
        };
    }

    const sendMessage = () => {
        stompClient.send("/app/message", {}, JSON.stringify({
            text: messageText,
            sender: {
                id: authorizedUser.id //FIXME: bad, server should do that
            },
            chat: {
                id: chatId
            }
        }));
    }

    const onInputKeyPress = (event) => {
        if (event.key === "Enter") {
            sendMessage();
        }
    }

    if (!chat) {
        return (
            <div>Loading...</div>
        );
    }
    return (
        <Chat messages={messages}
              messageText={messageText}
              authorizedUser={authorizedUser}
              currentPage={currentPage}
              pageSize={pageSize}
              chat={chat}
              setCurrentPage={setCurrentPage}
              loadMessages={loadMessages}
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
    isAuthorized: state.auth.isAuthorized,
    stompClient: state.stomp.stompClient
});

export default connect(mapStateToProps, {
    requestChatInfo,
    loadMessages,
    clearMessages,
    setCurrentPage,
    addMessages,
    initializeStompClient
})(ChatContainer);