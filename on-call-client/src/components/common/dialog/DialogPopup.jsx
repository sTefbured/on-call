import React from "react";
import styles from "./dialog.module.css";

const lockBody = () => {
    let bodyClasses = document.body.classList;
    if (!bodyClasses.contains('lockedByDialog')) {
        let padding = window.innerWidth - document.getElementById('root').offsetWidth + 'px';
        document.querySelectorAll('.lockPadding').forEach(el => el.style.paddingRight = padding);
        document.body.style.paddingRight = padding;
        bodyClasses.add('lockedByDialog');
    }
}

const unlockBody = () => {
    let bodyClasses = document.body.classList;
    document.querySelectorAll('.lockPadding').forEach(el => el.style.paddingRight = '0px');
    document.body.style.paddingRight = '0px';
    bodyClasses.remove('lockedByDialog');
}

const DialogPopup = (props) => {
    let dialogStyles = styles.dialog;
    if (props.isActive) {
        dialogStyles += ' ' + styles.active;
        lockBody();
    } else if (document.body.classList.contains('lockedByDialog')) {
        setTimeout(unlockBody, 500);
    }
    const close = (e) => {
        let classes = e.target.classList;
        if (classes.contains(styles.dialogBody) || classes.contains(styles.dialogClose)) {
            props.setIsActive(false);
            setTimeout(unlockBody, 500);
        }
    }
    return (
        <div className={dialogStyles}>
            <div className={styles.dialogBody} onClick={close}>
                <div className={styles.dialogContent}>
                    <div className={styles.dialogClose} onClick={close}>X</div>
                    <div className={styles.dialogTitle}>
                        {props.title}
                    </div>
                    <div className={styles.dialogText}>
                        {props.children}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default DialogPopup;