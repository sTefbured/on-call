import styles from "./notifications.module.css";
import NotificationsButton from "./NotificationsButton";
import Notification from "./notification/Notification";
import {useEffect, useState} from "react";

const Notifications = (props) => {
    let [isNotifying, setIsNotifying] = useState(false);
    useEffect(() => {
        setIsNotifying(props.notifications.some(n => n.isActive));
    }, [props.notifications]);

    let notificationElements = props.notifications.map(notification =>
        <Notification key={notification.id} notification={notification}/>
    );

    const setIsActive = (isActive, event) => {
        if (event != null && event?.relatedTarget?.className === styles.notification) {
            return
        }
        props.setIsActive(isActive);
        if (!isActive) {
            props.readNotifications(props.notifications);
        }
    }

    let notificationsStyles = styles.notifications;
    if (props.isActive) {
        notificationsStyles += ' ' + styles.active;
    }
    return (
        <div className={notificationsStyles} tabIndex="0" onBlur={(e) => setIsActive(false, e)}>
            <NotificationsButton isNotifying={isNotifying} onClick={() => setIsActive(!props.isActive)}/>
            <div className={styles.notificationsListWrapper}>
                <div className={styles.notificationsList}>
                    {notificationElements}
                </div>
            </div>
        </div>
    );
}

export default Notifications;