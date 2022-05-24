import {connect} from "react-redux";
import {useEffect, useState} from "react";
import {initializeStompClient} from "../../../../redux/reducers/stompReducer";
import {
    addNotifications,
    loadNotifications,
    readAllNotifications
} from "../../../../redux/reducers/notificationsReducer";
import Notifications from "./Notifications";

const NotificationsContainer = (props) => {
    let [isActive, setIsActive] = useState(false);

    useEffect(() => {
        if (props.isAuthorized) {
            props.loadNotifications(props.authorizedUser.id);
        }
    }, [props.isAuthorized]);

    useEffect(() => {
        if (props.stompClient) {
            let subscription = props.stompClient.subscribe(`/user/${props.authorizedUser.id}/notification`, notification => {
                let notificationObject = JSON.parse(notification.body);
                props.addNotifications([notificationObject]);
            });
            return () => {
                subscription.unsubscribe();
            };
        } else {
            props.initializeStompClient();
        }
    }, [props.stompClient]);

    return (
        <Notifications notifications={props.notifications}
                       isActive={isActive}
                       setIsActive={(isActive) => setIsActive(isActive)}
                       readNotifications={props.readAllNotifications}/>
    );
}

let mapStateToProps = (state) => ({
    notifications: state.notifications.notifications,
    authorizedUser: state.auth.user,
    isAuthorized: state.auth.isAuthorized,
    stompClient: state.stomp.stompClient
});

export default connect(mapStateToProps, {
    initializeStompClient,
    addNotifications,
    loadNotifications,
    readAllNotifications
})(NotificationsContainer);