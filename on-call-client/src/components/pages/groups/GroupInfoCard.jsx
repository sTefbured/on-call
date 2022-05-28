import styles from "./groups.module.css"
import {NavLink} from "react-router-dom";

const GroupLink = (props) => {
    return <NavLink to={"/groups" + props.id}>{props.children}</NavLink>
}

const GroupInfoCard = (props) => {
    return (
        <div className={styles.listRow}>
            <GroupLink id={props.group.idTag}><img className={styles.avatar} src={props.group.avatarUrl} alt=""/></GroupLink>
            <GroupLink id={props.group.idTag}><div>{props.group.name}</div></GroupLink>
        </div>
    );
}

export default GroupInfoCard;