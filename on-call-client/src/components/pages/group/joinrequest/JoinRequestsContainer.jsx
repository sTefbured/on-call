import {connect} from "react-redux";
import {getAllJoinRequests, approveJoinRequest} from "../../../../redux/reducers/groupsReducer";
import JoinRequests from "./JoinRequests";
import {useEffect, useState} from "react";

const PAGE_SIZE = 20;

const JoinRequestsContainer = (props) => {
    let [page, setPage] = useState(0);
    useEffect(() => props.getAllJoinRequests(props.group.id, page, PAGE_SIZE), [page]);
    return (
        <JoinRequests joinGroupRequests={props.joinGroupRequests} totalCount={props.totalCount}
                      page={page} setPage={setPage} pageSize={PAGE_SIZE} approveRequest={props.approveJoinRequest}/>
    );
}

let mapStateToProps = (state) => ({
    joinGroupRequests: state.group.joinGroupRequests,
    group: state.group.group,
    totalCount: state.group.totalCount
});

export default connect(mapStateToProps, {
    getAllJoinRequests,
    approveJoinRequest
})(JoinRequestsContainer);