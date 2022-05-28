import styles from "./group.module.css";
import GroupInformationContainer from "./groupinformation/GroupInformationContainer";
import GroupTabPanel from "./GroupTabPanel";

const Group = (props) => {
    return (
        <div className={styles.group}>
            <GroupTabPanel group={props.group}/>
            <div className={styles.groupInfo}><GroupInformationContainer/></div>
        </div>
    );
}

export default Group;