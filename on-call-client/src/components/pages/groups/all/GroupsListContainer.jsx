import React from "react";
import CollectionGrid from "../../../common/collectiongrid/CollectionGrid";
import GroupInfoCard from "../GroupInfoCard";
import {connect} from "react-redux";
import {getAllGroups, setCurrentPage} from "../../../../redux/reducers/groupsReducer";

class GroupsListContainer extends React.Component {
    componentDidMount() {
        this.props.getAllGroups(this.props.currentPage, this.props.pageSize);
    }

    onPageChanged = (pageNumber) => {
        this.props.setCurrentPage(pageNumber);
        this.props.getAllGroups(pageNumber, this.props.pageSize)
    }

    render() {
        let groupComponents = this.props.groups.map(group => (<div key={group.id}><GroupInfoCard group={group}/></div>));
        return (
            <CollectionGrid totalCount={this.props.totalCount}
                            pageSize={this.props.pageSize}
                            currentPage={this.props.currentPage}
                            onPageChanged={n => this.onPageChanged(n)}>
                {groupComponents}
            </CollectionGrid>
        );
    }
}


let mapStateToProps = (state) => {
    return {
        groups: state.group.groups,
        totalCount: state.group.totalCount,
        currentPage: state.group.currentPage,
        pageSize: state.group.pageSize
    };
}

export default connect(mapStateToProps, {
    getAllGroups,
    setCurrentPage
})(GroupsListContainer);