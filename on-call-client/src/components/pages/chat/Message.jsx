import styles from "./chat.module.css";
import {Link} from "react-router-dom";

const Message = ({message, authorizedUser}) => {
    let messageStyles = styles.message;
    if (authorizedUser.id === message.sender.id) {
        messageStyles += ' ' + styles.sentByCurrentUser;
    }
    return (
        <div className={messageStyles}>
            <div className={styles.avatarThumbnail}>
                <img src={message.sender.avatarThumbnailUrl} alt=""/>
            </div>
            <div className={styles.messageContent}>
                <div className={styles.messageInfo}>
                    <Link to={"/users/" + message.sender.id}>{message.sender.firstName}</Link>
                    <span>{new Date(message.sendingDateTime).toLocaleString()}</span>
                </div>
                <div className={styles.messageText}>
                    {message.text}
                </div>
            </div>
        </div>
    );
}

export default Message;