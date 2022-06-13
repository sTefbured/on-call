import styles from "../notifications.module.css";

const ScheduledEventStartNotificationContent = (props) => {
    return (
        <>
            <div className={styles.notificationContent}>
                <div className={styles.notificationText}>{props.notification.notificationText}</div>
            </div>
        </>
    );
}

export default ScheduledEventStartNotificationContent;