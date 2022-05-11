import React from "react";
import {connect} from "react-redux";
import styles from "./createjoinrequest.module.css";
import DialogPopup from "../../../../common/dialog/DialogPopup";
import {createJoinRequest, setJoinRequestMessage} from "../../../../../redux/reducers/groupsReducer";
import Button from "../../../../common/button/Button";
import TextArea from "../../../../common/textarea/TextArea";

class CreateJoinRequestDialogContainer extends React.Component {
    render() {
        return (
            <DialogPopup isActive={this.props.isActive}
                         setIsActive={this.props.setIsActive}
                         title='Create join request'>
                <div className={styles.joinRequest}>
                    <TextArea onChange={(e) => this.props.setJoinRequestMessage(e.target.value)}
                              placeholder='Enter your message to administrators of the group'>
                        {this.props.joinGroupRequest.message}
                    </TextArea>
                    <Button onClick={() => {
                        this.props.createJoinRequest(this.props.groupId,
                            this.props.authorizedUser, this.props.joinGroupRequest.message);
                        this.props.setIsActive(false);
                        this.props.setJoinRequestMessage('');
                    }}>
                        Create
                    </Button>
                </div>
            </DialogPopup>
        )
    }
}

let mapStateToProps = (state) => ({
    authorizedUser: state.auth.user,
    joinGroupRequest: state.group.joinGroupRequest,
});

export default connect(mapStateToProps, {
    setJoinRequestMessage,
    createJoinRequest
})(CreateJoinRequestDialogContainer);