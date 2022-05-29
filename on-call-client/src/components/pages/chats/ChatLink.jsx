import {Link} from "react-router-dom";
import styles from "./chats.module.css";

const ChatLink = (props) => {
    let chat = props.chat;
    let images = chat.usersGrants.map(grant => {
        return (
            <img key={grant.user.id} src={grant.user.avatarThumbnailUrl} alt=""/>
        );
    });
    return (
        <Link to={props.to}>
            <div className={styles.chatLink}>
                {chat.name}
                <div>{images}</div>
                <div></div>
            </div>
        </Link>
    )
}

export default ChatLink;