import styles from "../group.module.css";
import Button from "../../../common/button/Button";
import CollectionGrid from "../../../common/collectiongrid/CollectionGrid";
import NewChatDialogContainer from "./NewChatDialogContainer";
import {useState} from "react";
import {Link} from "react-router-dom";
import ChatLink from "../../chats/ChatLink";


const GroupChatsTab = (props) => {
    let [isCreationDialogOpened, setIsCreationDialogOpened] = useState(false);
    let newChatPanel;
    let chats;
    if (!props.group) {
        return;
    }
    newChatPanel = props.hasCreateChatPermission
        ? (
            <div>
                <NewChatDialogContainer isActive={isCreationDialogOpened} createChat={props.createChat}
                                        setIsActive={(isActive) => setIsCreationDialogOpened(isActive)}
                                        group={props.group}/>
                <Button onClick={() => setIsCreationDialogOpened(true)}>Create new chat</Button>
            </div>
        )
        : <></>;

    chats = props.hasMemberPermission
        ? (
            <CollectionGrid>
                {
                    props.chats.map(chat => (
                        <ChatLink key={chat.id}
                                  to={"/groups" + props.group.idTag + "?tab=chats&id=" + chat.id} chat={chat}/>
                    ))
                }
            </CollectionGrid>
        )
        : <></>;
    return (
        <div>
            {newChatPanel}
            {chats}
        </div>
    );
}

export default GroupChatsTab;