import styles from "./menu.module.css";
import {NavLink} from "react-router-dom";

const StyledNavLink = (props) => {
    return (
        <NavLink {...props} className={styles.link}>{props.children}</NavLink>
    )
}

const Menu = () => {
    return (
        <nav className={styles.menu}>
            <StyledNavLink to="/users">Users</StyledNavLink>
            <StyledNavLink to="/groups">Groups</StyledNavLink>
            <StyledNavLink to="/help">Help</StyledNavLink>
            <StyledNavLink to="/about">About</StyledNavLink>
        </nav>
    );
}

export default Menu;
