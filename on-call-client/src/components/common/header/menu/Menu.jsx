import styles from "./menu.module.css";

const Menu = () => {
    return (
        <nav className={styles.menu}>
            <div>Users</div>
            <div>Groups</div>
            <div>Help</div>
            <div>About</div>
        </nav>
    );
}

export default Menu;
