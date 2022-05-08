import styles from "./button.module.css"

const Button = (props) => {
    let buttonStyles = styles.button;
    if (props.className) {
        buttonStyles += ' ' + props.className;
    }
    return (
        <button className={buttonStyles} type={props?.type} onClick={props?.onClick}>{props.children}</button>
    );
}

export default Button;