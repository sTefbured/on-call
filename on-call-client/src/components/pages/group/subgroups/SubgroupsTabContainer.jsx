import {connect} from "react-redux";
import CollectionGrid from "../../../common/collectiongrid/CollectionGrid";
import {setCurrentPage} from "../../../../redux/reducers/groupsReducer";
import GroupInfoCard from "../../groups/GroupInfoCard";

const SubgroupsTabContainer = (props) => {
    if (props.group) {
        let groupElements = props.group.childGroups.map(group => {
            let outputGroup = {...group, idTag: props.group.idTag + "/" + group.idTag}
            return <GroupInfoCard key={group.id} group={outputGroup}/>
        });
        return (
            <CollectionGrid totalCount={props.group.childGroups.length}
                            pageSize={props.pageSize}
                            currentPage={props.page}
                            onPageChanged={n => props.setPage(n)}>
                {groupElements}
            </CollectionGrid>
        );
    }
    return <></>
}

let mapStateToProps = (state) => ({
    pageSize: state.group.pageSize,
    currentPage: state.group.currentPage,
    group: state.group.group
});

export default connect(mapStateToProps, {
    setCurrentPage
})(SubgroupsTabContainer);