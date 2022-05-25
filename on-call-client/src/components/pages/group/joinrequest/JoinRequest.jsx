import styles from "./joinrequest.module.css"
import Button from "../../../common/button/Button";

const JoinRequest = (props) => {
    return (
        <div className={styles.joinRequest}>
            <div className={styles.userInfo}>
                <img src={props.request.user.avatarThumbnailUrl} alt=""/>
                <div>{props.request.user.firstName + " " + props.request.user.lastName + " wants to join the group"}</div>
            </div>
            <div className={styles.message}>{props.request.message}</div>
            <Button className={styles.button} onClick={props.approveRequest}>Approve</Button>
        </div>
    );
}

export default JoinRequest;