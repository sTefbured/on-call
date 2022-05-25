import styles from "../notifications.module.css";
import {useNavigate} from "react-router-dom";
import NewMessageNotificationContent from "./NewMessageNotificationContent";
import ScheduledEventStartNotificationContent from "./ScheduledEventStartNotificationContent";
import NewJoinGroupRequestNotificationContent from "./NewJoinGroupRequestNotificationContent";

const Notification = (props) => {
    let content;
    let link;
    let navigate = useNavigate();
    switch (props.notification.notificationType.name) {
        case "message": {
            link = "/chats/" + props.notification.sourceObjectId;
            content = <NewMessageNotificationContent notification={props.notification}/>;
            break;
        }
        case "scheduledEvent": {
            link = "/schedule";
            content = <ScheduledEventStartNotificationContent notification={props.notification}/>;
            break;
        }
        case "joinGroupRequest": {
            link = "/groups/" + props.notification.sourceObjectId + "?tab=join_requests";
            content = <NewJoinGroupRequestNotificationContent notification={props.notification}/>;
            break;
        }
        default: {
            content = <>{props.notification.message}</>
        }
    }
    return (
        <div className={styles.notification} onClick={() => navigate(link)}>
            {content}
        </div>
    );
}

export default Notification;