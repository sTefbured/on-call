import DialogPopup from "../../../common/dialog/DialogPopup";
import TextInput from "../../../common/textinput/TextInput";
import Button from "../../../common/button/Button";
import styles from "../group.module.css";
import {useState} from "react";

const NewChatDialogContainer = (props) => {
    let [name, setName] = useState("");
    return (
        <DialogPopup title="Create new chat"
                     isActive={props.isActive} setIsActive={props.setIsActive}>
            <div className={styles.dialogRow}>
                <label>Name</label>
                <TextInput value={name} onChange={e => setName(e.target.value)}/>
            </div>
            <div className={styles.dialog}>
                <Button onClick={() => props.createChat({name, group: props.group})}>Create</Button>
            </div>
        </DialogPopup>
    );
}

export default NewChatDialogContainer;