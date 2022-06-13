import styles from "./login.module.css"
import TextInput from "../../common/textinput/TextInput";

const LoginForm = (props) => {
    return (
        <div className={styles.loginForm}>
            <div>
                <label>Username</label>
                <TextInput onChange={e => props.setUsername(e.target.value)} value={props.username}/>
            </div>
            <div>
                <label>Password</label>
                <TextInput onChange={e => props.setPassword(e.target.value)} value={props.password} type="password"/>
            </div>
            <button onClick={() => props.login(props.username, props.password)}>Login</button>
        </div>
    )
}

export default LoginForm;