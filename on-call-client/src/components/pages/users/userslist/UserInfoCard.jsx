import styles from "./userslist.module.css"
import {NavLink} from "react-router-dom";

const UserInfoCard = (props) => {
    let UserLink = (prop) => {
        return <NavLink to={"/users/" + props.user.id}>{prop.children}</NavLink>
    }
    return (
        <div className={styles.list__row}>
            <UserLink><img className={styles.avatar} src={props.user.avatarUrl} alt=""/></UserLink>
            <UserLink><div>{props.user.firstName + ' ' + props.user.lastName}</div></UserLink>
            <UserLink><div>{props.user.username}</div></UserLink>
        </div>
    );
}

export default UserInfoCard;