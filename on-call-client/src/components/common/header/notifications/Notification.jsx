import styles from "./notifications.module.css";
import {Link, useNavigate} from "react-router-dom";

const Notification = (props) => {
    let content;
    let link;
    let navigate = useNavigate();
    switch (props.notification.notificationType.name.toLowerCase()) {
        case "message": {
            link = "/chats/" + props.notification.sourceObjectId;
            content = (
                <>
                    <div className={styles.avatarWrapper}>
                        <img className={styles.avatar} src={props.notification.creator.avatarThumbnailUrl} alt=""/>
                    </div>
                    <div className={styles.notificationContent}>
                        <div>
                            <span>A new message from </span>
                            <Link className={styles.textLink}
                                  to={"/users/" + props.notification.creator.id}>{props.notification.creator.username}</Link>
                        </div>
                        <div className={styles.notificationText}>{props.notification.notificationText}</div>
                    </div>
                </>
            )
            break;
        }
        default: {
            content = <></>
        }
    }
    return (
        <div className={styles.notification} onClick={() => navigate(link)}>
            {content}
        </div>
    );
}

export default Notification;