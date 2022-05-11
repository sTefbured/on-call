import styles from "./textarea.module.css";

const TextArea = (props) => {
    return (
        <textarea {...props}
                  className={styles.textarea + ' ' + props.className}
                  value={props.children}/>
    );
}

export default TextArea;