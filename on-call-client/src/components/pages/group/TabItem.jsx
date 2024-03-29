import styles from "./group.module.css";
import {NavLink, useLocation} from "react-router-dom";

const TabItem = (props) => {
    let location = useLocation();
    let url = location.pathname + location.search;
    return (
        <NavLink className={() => {
            if (props.exact) {
                return url === props.to ? styles.item + " " + styles.active : styles.item;
            }
            return url.startsWith(props.to) ? styles.item + " " + styles.active : styles.item;
        }} end to={props.to} >
            <div>
                {props.children}
            </div>
        </NavLink>
    );
}

export default TabItem;