import styles from "./notifications.module.css";
import {useNavigate} from "react-router-dom";
import NewMessageNotificationContent from "./notification/NewMessageNotificationContent";
import ScheduledEventStartNotificationContent from "./notification/ScheduledEventStartNotificationContent";

const Notification = (props) => {
    let content;
    let link;
    let navigate = useNavigate();
    switch (props.notification.notificationType.name) {
        case "message": {
            link = "/chats/" + props.notification.sourceObjectId;
            content = <NewMessageNotificationContent notification={props.notification}/>
            break;
        }
        case "scheduledEvent": {
            link = "/schedule";
            content = <ScheduledEventStartNotificationContent notification={props.notification}/>;
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