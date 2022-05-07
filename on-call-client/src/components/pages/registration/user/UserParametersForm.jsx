import styles from "../registration.module.css"

const UserParametersForm = (props) => {
    return (
        <div className={styles.form}>
            <div>
                <label>First name</label>
                <input name="firstName" value={props.user.firstName} onChange={(e) => props.changeFirstName(e.target.value)}/>
            </div>
            <div>
                <label>Last name</label>
                <input name="lastName" value={props.user.lastName} onChange={(e) => props.changeLastName(e.target.value)}/>
            </div>
            <div>
                <label>Username</label>
                <input name="username" value={props.user.username} onChange={(e) => props.changeUsername(e.target.value)}/>
            </div>
            <div>
                <label>Email</label>
                <input name="email" value={props.user.email} onChange={(e) => props.changeEmail(e.target.value)}/>
            </div>
            <div>
                <input type="file" onChange={(e) => props.changeAvatar(e.target.files[0])}/>
            </div>
            <div>
                <label>Password</label>
                <input name="password" type="password"
                       value={props.user.password} onChange={(e) => props.changePassword(e.target.value)}/>
            </div>
            <div>
                <label>Birth date</label>
                <input name="birthDate" type="date"
                       value={props.user.birthDate} onChange={(e) => props.changeBirthDate(e.target.value)}/>
            </div>
            <button onClick={() => props.register(props.user, props.avatar)}>Register</button>
        </div>
    );
}

export default UserParametersForm;