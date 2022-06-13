import {connect} from "react-redux";
import GroupChatsTab from "./GroupChatsTab";
import {useEffect, useState} from "react";
import {createChat, queryChatsForGroup, setCurrentPage} from "../../../../redux/reducers/chatReducer";
import {
    GROUP_CHAT_CREATE_PERMISSION,
    GROUP_MEMBER_VIEW_PERMISSION,
    hasUserPermissionForGroup
} from "../../../../utils/onCallUtils";
import {useSearchParams} from "react-router-dom";
import ChatContainer from "../../chat/ChatContainer";

const GroupChatsTabContainer = (props) => {
    let [hasCreateChatPermission, setHasCreateChatPermission] = useState(false);
    let [hasMemberPermission, setHasMemberPermission] = useState(false);
    let [searchParams] = useSearchParams();
    let chatId = searchParams.get("id");
    useEffect(() => {
        if (!props.group || !props.isAuthorized) {
            return
        }
        setHasMemberPermission(hasUserPermissionForGroup(props.authorizedUser, GROUP_MEMBER_VIEW_PERMISSION, props.group.id));
        setHasCreateChatPermission(hasUserPermissionForGroup(props.authorizedUser, GROUP_CHAT_CREATE_PERMISSION, props.group.id));
    }, [props.group, props.authorizedUser, props.currentPage]);

    useEffect(() => {
        if (props.group && hasMemberPermission) {
            props.queryChatsForGroup(props.group.id, props.currentPage, props.pageSize);
        }
    }, [props.group, props.currentPage, hasMemberPermission]);
    if (!props.group) {
        return <></>
    }
    if (chatId) {
        return (
            <ChatContainer chatId={chatId}/>
        );
    }
    return (
        <GroupChatsTab hasMemberPermission={hasMemberPermission} hasCreateChatPermission={hasCreateChatPermission}
                       chats={props.chats} setCurrentPage={props.setCurrentPage} createChat={props.createChat}
                       group={props.group}/>
    );
}

let mapStateToProps = (state) => ({
    chats: state.chat.chats,
    group: state.group.group,
    currentPage: state.chat.currentPage,
    pageSize: state.chat.defaultPageSize,
    authorizedUser: state.auth.user,
    isAuthorized: state.auth.isAuthorized
});

export default connect(mapStateToProps, {
    queryChatsForGroup,
    setCurrentPage,
    createChat
})(GroupChatsTabContainer);