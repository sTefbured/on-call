import {connect} from "react-redux";
import styles from "../../group/groupinformation/createjoinrequestdialog/createjoinrequest.module.css";
import TextArea from "../../../common/textarea/TextArea";
import Button from "../../../common/button/Button";
import DialogPopup from "../../../common/dialog/DialogPopup";
import React from "react";
import TextInput from "../../../common/textinput/TextInput";
import {setGroupDescription, setGroupIdTag, setGroupName, createGroup} from "../../../../redux/reducers/groupsReducer";

const CreateNewGroupDialogContainer = (props) => {
    return (
        <DialogPopup isActive={props.isActive}
                     setIsActive={props.setIsActive}
                     title='Create new group'>
            <div className={styles.joinRequest}>
                <div>
                    <label>Name</label>
                    <TextInput value={props.name} onChange={e => props.setGroupName(e.target.value)}/>
                </div>
                <div>
                    <label>Id tag</label>
                    <TextInput value={props.idTag} onChange={e => props.setGroupIdTag(e.target.value)}/>
                </div>
                <div>
                    <label>Description</label>
                    <TextArea value={props.description} onChange={e => props.setGroupDescription(e.target.value)}/>
                </div>
                <Button onClick={() => props.createGroup({
                    name: props.name,
                    idTag: props.idTag,
                    description: props.description
                })}>Create</Button>
            </div>
        </DialogPopup>
    )
}

let mapStateToProps = (state) => ({
    name: state.group.name,
    idTag: state.group.idTag,
    description: state.group.description
});

export default connect(mapStateToProps, {
    setGroupName,
    setGroupIdTag,
    setGroupDescription,
    createGroup
})(CreateNewGroupDialogContainer);