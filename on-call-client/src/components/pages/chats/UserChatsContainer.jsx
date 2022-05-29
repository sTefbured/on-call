import styles from "./chats.module.css";
import {connect} from "react-redux";
import {queryChatsForUser, setCurrentPage} from "../../../redux/reducers/chatReducer";
import {useEffect} from "react";
import ChatLink from "./ChatLink";

const UserChatsContainer = (props) => {
    useEffect(() => {
        if (props.isAuthorized) {
            props.queryChatsForUser(props.authorizedUser.id, props.currentPage, props.pageSize);
        }
    }, [props.isAuthorized]);
    let chatLinks = props.chats.map(chat => {
            return <ChatLink key={chat.id} to={"/chats/" + chat.id} chat={chat}/>
        }
    );
    return (
        <div className={styles.chatLinksContainer}>{chatLinks}</div>
    );
}

let mapStateToProps = (state) => ({
    chats: state.chat.chats,
    currentPage: state.chat.currentPage,
    pageSize: state.chat.pageSize,
    authorizedUser: state.auth.user,
    isAuthorized: state.auth.isAuthorized
});

export default connect(mapStateToProps, {
    setCurrentPage,
    queryChatsForUser
})(UserChatsContainer);