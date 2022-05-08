import styles from "./buttonspanel.module.css"
import {NavLink} from "react-router-dom";
import Button from "../../button/Button";

const ButtonsPanel = () => {
    return (
        <div className={styles.panel}>
            <NavLink to="/login"><Button>Login</Button></NavLink>
            <NavLink to="/register"><Button>Register</Button></NavLink>
        </div>
    )
}

export default ButtonsPanel;