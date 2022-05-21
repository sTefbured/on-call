import styles from "./chat.module.css"
import Message from "./Message";
import TextInput from "../../common/textinput/TextInput";
import {useEffect, useRef, useState} from "react";

const Chat = (props) => {
    let [isHandlingScroll, setIsHandlingScroll] = useState(false);
    let bottomRef = useRef();
    let scrollableElementRef = useRef();
    let messages = props.messages.map(message => (
        <Message key={message.id} message={message} authorizedUser={props.authorizedUser}/>
    ));
    const scrollToBottom = () => {
        bottomRef.current.scrollIntoView({behavior: "smooth"});
    }

    const scrollHandler = () => {
        if (scrollableElementRef.current.scrollTop === 0 && !isHandlingScroll) {
            setIsHandlingScroll(true);
            props.setCurrentPage(props.currentPage + 1);
            props.loadMessages(props.chat.id, props.currentPage + 1, props.pageSize)
                .then(() => setIsHandlingScroll(false));
        }
    }

    useEffect(() => {
        scrollToBottom()
    }, [props.messages]);
    return (
        <div className={styles.chat}>
            <div className={styles.chatToolbar}>
                <h1>{props.chat.name}</h1>
            </div>
            <div className={styles.chatContainer}>
                <div className={styles.messagesContainerWrapper}>
                    <div className={styles.messagesContainer} onScroll={scrollHandler} ref={scrollableElementRef}>
                        {messages}
                        <div ref={bottomRef}/>
                    </div>
                </div>
                <div className={styles.inputPanel}>
                    <TextInput className={styles.messageInput}
                               value={props.messageText}
                               onChange={(e) => props.setMessageText(e.target.value)}
                               onKeyPress={e => props.onInputKeyPress(e)}/>
                </div>
            </div>
        </div>
    );
}

export default Chat;