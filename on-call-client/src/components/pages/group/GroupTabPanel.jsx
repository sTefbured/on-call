import styles from "./group.module.css";
import TabItem from "./TabItem";

const GroupTabPanel = (props) => {
    if (!props.group) {
        return <></>
    }
    return (
        <div className={styles.tabs}>
            <TabItem to={"/groups" + props.group.idTag}>Group information</TabItem>
            <TabItem to={"/groups" + props.group.idTag + "?tab=members"}>Members</TabItem>
            <TabItem to={"/groups" + props.group.idTag + "?tab=subgroups"}>Subgroups</TabItem>
            <TabItem to={"/groups" + props.group.idTag + "?tab=chats"}>Chats</TabItem>
        </div>
    );
}

export default GroupTabPanel;