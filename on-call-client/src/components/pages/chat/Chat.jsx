import styles from "./chat.module.css"
import Message from "./Message";
import TextInput from "../../common/textinput/TextInput";
import {useEffect, useRef, useState} from "react";

const Chat = ({messages, messageText, authorizedUser, currentPage, pageSize, chat, setCurrentPage,
                  loadMessages, setMessageText, onInputKeyPress}) => {
    let [isHandlingScroll, setIsHandlingScroll] = useState(false);
    let bottomRef = useRef();
    let scrollableElementRef = useRef();
    let messagesElements = messages.map(message => (
        <Message key={message.id} message={message} authorizedUser={authorizedUser}/>
    ));
    const scrollToBottom = () => {
        bottomRef.current.scrollIntoView({behavior: "smooth"});
    }

    const scrollHandler = () => {
        if (scrollableElementRef.current.scrollTop === 0 && !isHandlingScroll) {
            setIsHandlingScroll(true);
            setCurrentPage(currentPage + 1);
            loadMessages(chat.id, currentPage + 1, pageSize)
                .then(() => setIsHandlingScroll(false));
        }
    }

    useEffect(() => {
        scrollToBottom()
    }, [messages]);
    return (
        <div className={styles.chat}>
            <div className={styles.chatToolbar}>
                <h1>{chat.name}</h1>
            </div>
            <div className={styles.chatContainer}>
                <div className={styles.messagesContainerWrapper}>
                    <div className={styles.messagesContainer} onScroll={scrollHandler} ref={scrollableElementRef}>
                        {messagesElements}
                        <div ref={bottomRef}/>
                    </div>
                </div>
                <div className={styles.inputPanel}>
                    <TextInput className={styles.messageInput}
                               value={messageText}
                               onChange={(e) => setMessageText(e.target.value)}
                               onKeyPress={e => onInputKeyPress(e)}/>
                </div>
            </div>
        </div>
    );
}

export default Chat;