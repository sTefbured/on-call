import styles from "../notifications.module.css";
import {Link} from "react-router-dom";

const NewMessageNotificationContent = (props) => {
    return (
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
    );
}

export default NewMessageNotificationContent;