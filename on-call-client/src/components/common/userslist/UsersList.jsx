import styles from "./userslist.module.css";
import React from "react";
import UserInfoRow from "./UserInfoRow";

const UsersList = (props) => {
    let pagesCount = Math.ceil(props.totalCount / props.pageSize);
    let pageNumbers = [];
    for (let i = 0; i < pagesCount; i++) {
        let className = styles.pageNumber;
        className = props.currentPage === i ? className + ' ' + styles.currentPage : className;
        let pageNumber = <div onClick={() => {props.onPageChanged(i)}} key={i} className={className}>{i + 1}</div>;
        pageNumbers = [...pageNumbers, pageNumber];
    }
    let userComponents = props.users.map(user => (<div key={user.id}><UserInfoRow user={user} /></div>));
    return (
        <div>
            <div>{pageNumbers}</div>
            <div className={styles.list}>{userComponents}</div>
        </div>
    )
}

export default UsersList;