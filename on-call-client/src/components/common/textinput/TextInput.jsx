import styles from "./textinput.module.css";

const TextInput = (props) => {
    return (
        <input {...props} className={styles.input + ' ' + props.className}/>
    )
}

export default TextInput;