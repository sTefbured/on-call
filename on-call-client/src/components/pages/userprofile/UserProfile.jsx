import styles from "./user.module.css";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect} from "react";
import Button from "../../common/button/Button";

const UserProfile = (props) => {
    let params = useParams();
    let navigate = useNavigate();
    useEffect(() => props.getUserById(params.id), [params]);
    useEffect(() => {
        if (props.navigateToDialog) {
            navigate("/chats/" + props.chat.id);
        }
    }, [props.navigateToDialog])
    return !props.user
        ? <></>
        : (
            <div className={styles.user}>
                <div>
                    <img src={props.user.avatarUrl} alt=""/>
                    <Button onClick={() => props.loadChat(props.user.id)}>Chat</Button>
                </div>
                <div className={styles.userInfo}>
                    <div className={styles.nameContainer}>
                        <h1>{props.user.firstName + " " + props.user.lastName}</h1>
                        <h2>{props.user.username}</h2>
                    </div>
                    {
                        (props.user.email)
                            ? <div>
                                <label>Email:</label>
                                <span>{props.user.email}</span>
                            </div>
                            : <></>
                    }
                    <div>
                        <label>Birth date:</label>
                        <span>{props.user.birthDate}</span>
                    </div>
                </div>
            </div>
        )
}

export default UserProfile;