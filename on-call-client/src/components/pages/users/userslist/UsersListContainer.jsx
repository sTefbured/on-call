import {connect} from "react-redux";
import {getAllUsers, setCurrentPage} from "../../../../redux/reducers/usersReducer";
import React from "react";
import CollectionGrid from "../../../common/collectiongrid/CollectionGrid";
import UserInfoCard from "./UserInfoCard";

class UsersListContainer extends React.Component {
    componentDidMount() {
        this.props.getAllUsers(this.props.currentPage, this.props.pageSize);
    }

    onPageChanged = (pageNumber) => {
        this.props.setCurrentPage(pageNumber);
        this.props.getAllUsers(pageNumber, this.props.pageSize)
    }

    render() {
        let userComponents = this.props.users.map(user => (<div key={user.id}><UserInfoCard user={user}/></div>));
        return (
            <CollectionGrid totalCount={this.props.totalCount}
                            pageSize={this.props.pageSize}
                            currentPage={this.props.currentPage}
                            onPageChanged={n => this.onPageChanged(n)}>
                {userComponents}
            </CollectionGrid>
        );
    }
}

let mapStateToProps = (state) => {
    return {
        users: state.usersPage.users,
        totalCount: state.usersPage.totalCount,
        currentPage: state.usersPage.currentPage,
        pageSize: state.usersPage.pageSize
    };
}

export default connect(mapStateToProps, {
    getAllUsers,
    setCurrentPage
})(UsersListContainer);