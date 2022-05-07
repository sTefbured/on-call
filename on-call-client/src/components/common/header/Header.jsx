import globalStyles from "../../../app.module.css"
import styles from "./header.module.css"
import Menu from "./menu/Menu";

const Header = () => {
    return (
        <header className={styles.header}>
            <div className={`${globalStyles.container}`}>
                <img src='https://static.independent.co.uk/2021/12/07/10/PRI213893584.jpg?quality=75&width=990&auto=webp&crop=982:726,smart' alt=''/>
                <Menu/>
            </div>

        </header>
    )
}

export default Header;