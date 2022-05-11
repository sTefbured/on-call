import {connect} from "react-redux";
import GroupInformation from "./GroupInformation";
import {getGroupByTagSequence} from "../../../../redux/reducers/groupsReducer";

let mapStateToProps = (state) => ({
    group: state.group.group,
    isMember: state.group.isMember
});

export default connect(mapStateToProps, {
    getGroupByTagSequence
})(GroupInformation);