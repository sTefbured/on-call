import styles from "./footer.module.css"
import globalStyles from "../../../app.module.css"

const Footer = () => {
    return (
        <footer className={styles.footer}>
            <div className={globalStyles.container}>
                <div className={styles.footer__row}>
                    <div>
                        Blah
                    </div>
                    <div>
                        Blah
                    </div>
                    <div>
                        Blah
                    </div>
                    <div>
                        Blah
                    </div>
                </div>
            </div>
        </footer>
    )
}

export default Footer;