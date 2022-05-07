import {connect} from "react-redux";
import {getAllUsers, setCurrentPage} from "../../../../redux/reducers/usersReducer";
import React from "react";
import UsersList from "../../../common/userslist/UsersList";
import {logout} from "../../../../redux/reducers/authReducer";

class UsersListContainer extends React.Component {
    componentDidMount() {
        this.props.getAllUsers(this.props.currentPage, this.props.pageSize);
    }

    onPageChanged = (pageNumber) => {
        this.props.setCurrentPage(pageNumber);
        this.props.getAllUsers(pageNumber, this.props.pageSize)
    }

    render() {
        return <UsersList totalCount={this.props.totalCount}
                          pageSize={this.props.pageSize}
                          currentPage={this.props.currentPage}
                          users={this.props.users}
                          onPageChanged={n => this.onPageChanged(n)}
        logout={this.props.logout}/>;
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
    setCurrentPage,
    logout
})(UsersListContainer);