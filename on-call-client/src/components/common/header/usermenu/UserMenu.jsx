import React from "react";
import styles from "./usermenu.module.css"
import {NavLink} from "react-router-dom";

class UserMenu extends React.Component {
    state = {
        isActive: false
    }

    setIsActive(isActive, event) {
        if (event != null && event?.relatedTarget?.className === styles.listItem) {
            return
        }
        this.setState({isActive: isActive});
    }

    render() {
        let menuStyles = styles.menu;
        if (this.state.isActive) {
            menuStyles += ' ' + styles.active;
        }
        return (
            <div className={menuStyles} tabIndex="0" onBlur={(e) => this.setIsActive(false, e)}>
                <div className={styles.menuButton}>
                    <div onClick={() => this.setIsActive(!this.state.isActive)}>
                        <img className={styles.avatar} src={this.props.avatarThumbnailUrl} alt=""/>
                        <div className={styles.arrowContainer}>
                            <div className={styles.arrow}/>
                        </div>
                    </div>
                </div>
                <div className={styles.listWrapper}>
                    <div className={styles.list}>
                        <NavLink end className={styles.listItem} to={"/users/" + this.props.user.id}>My profile</NavLink>
                        <NavLink end className={styles.listItem} to={"/chats"}>My chats</NavLink>
                        <NavLink end className={styles.listItem}
                            to={"/users/" + this.props.user.id + "/groups"}>My groups</NavLink>
                        <NavLink end className={styles.listItem} to={"/schedule"}>My schedule</NavLink>
                        <NavLink end className={styles.listItem} to={"/conferences"}>My conference rooms</NavLink>
                        <div className={styles.listItem} onClick={this.props.logout}>Log out</div>
                    </div>
                </div>
            </div>
        );
    }
}

export default UserMenu;