import styles from "./login.module.css"

const LoginForm = (props) => {
    return (
        <div className={styles.loginForm}>
            <div>
                <label>Username</label>
                <input onChange={e => props.setUsername(e.target.value)} value={props.username}/>
            </div>
            <div>
                <label>Password</label>
                <input onChange={e => props.setPassword(e.target.value)} value={props.password} type="password"/>
            </div>
            <button onClick={() => props.login(props.username, props.password)}>Login</button>
        </div>
    )
}

export default LoginForm;