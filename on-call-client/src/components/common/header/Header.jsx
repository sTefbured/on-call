import globalStyles from "../../../app.module.css"
import styles from "./header.module.css"
import Menu from "./menu/Menu";
import UserMenuContainer from "./usermenu/UserMenuContainer";
import ButtonsPanel from "./buttonspanel/ButtonsPanel";
import NotificationsContainer from "./notifications/NotificationsContainer";

const LOGO_PATH = "https://placekitten.com/100/100";

const Header = (props) => {
    let authorizedComponent = (
        <div>
            {/*<NotificationsContainer/>*/}
            <Menu/>
            <UserMenuContainer {...props.user}/>
        </div>
    );
    let unauthorizedComponent = (<ButtonsPanel/>);
    return (
        <header className={styles.header + ' lockPadding'}>
            <div className={`${globalStyles.container} ${styles.headerContent}`}>
                <img className={styles.logo} src={LOGO_PATH} alt=''/>
                {props.isAuthorized ? authorizedComponent : unauthorizedComponent}
            </div>

        </header>
    )
}

export default Header;