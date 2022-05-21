import {connect} from "react-redux";
import {queryChatsForUser, setCurrentPage} from "../../../redux/reducers/chatReducer";
import {useEffect} from "react";
import {Link} from "react-router-dom";

// FIXME: temporary solution with raw links
const UserChatsContainer = (props) => {
    useEffect(() => {
        if (props.isAuthorized) {
            props.queryChatsForUser(props.authorizedUser.id, props.currentPage, props.pageSize);
        }
    }, [props.isAuthorized]);
    let chatLinks = props.chats.map(chat => <Link key={chat.id} to={"/chats/" + chat.id}>{chat.name}</Link>);
    return (
        <div>{chatLinks}</div>
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