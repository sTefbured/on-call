import styles from "./group.module.css";
import GroupTabPanel from "./GroupTabPanel";

const Group = (props) => {
    return (
        <div className={styles.group}>
            <GroupTabPanel authorizedUser={props.authorizedUser} isAuthorized={props.isAuthorized} group={props.group}/>
            <div className={styles.groupInfo}>{props.children}</div>
        </div>
    );
}

export default Group;