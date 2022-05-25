import {useState} from "react";
import Button from "../../../common/button/Button";
import styles from "../group.module.css"
import CreateJoinRequestDialogContainer from "./createjoinrequestdialog/CreateJoinRequestDialogContainer";

const GroupInformation = (props) => {
    const [isCreateJoinRequestDialogActive, setIsCreateJoinRequestDialogActive] = useState(false);
    if (props.group) {
        return (
            <div className={styles.groupInfo}>
                <CreateJoinRequestDialogContainer groupId={props.group.id}
                                                  isActive={isCreateJoinRequestDialogActive}
                                                  setIsActive={(isActive) => setIsCreateJoinRequestDialogActive(isActive)}/>
                <h1>{props.group.name}</h1>
                <div className={styles.infoItem}>{props.group.description}</div>
                {
                    (!props.isMember)
                    && <Button onClick={() => setIsCreateJoinRequestDialogActive(true)}>Join</Button>
                }
            </div>
        );
    }
    return <></>
}

export default GroupInformation;