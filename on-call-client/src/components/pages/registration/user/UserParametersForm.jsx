import styles from "../registration.module.css"
import TextInput from "../../../common/textinput/TextInput";
import Button from "../../../common/button/Button";

const UserParametersForm = (props) => {
    return (
        <div className={styles.form}>
            <div>
                <label>First name</label>
                <TextInput value={props.user.firstName} onChange={(e) => props.changeFirstName(e.target.value)}/>
            </div>
            <div>
                <label>Last name</label>
                <TextInput value={props.user.lastName} onChange={(e) => props.changeLastName(e.target.value)}/>
            </div>
            <div>
                <label>Username</label>
                <TextInput value={props.user.username} onChange={(e) => props.changeUsername(e.target.value)}/>
            </div>
            <div>
                <label>Email</label>
                <TextInput value={props.user.email} onChange={(e) => props.changeEmail(e.target.value)}/>
            </div>
            <div>
                <TextInput type="file" onChange={(e) => props.changeAvatar(e.target.files[0])}/>
            </div>
            <div>
                <label>Password</label>
                <TextInput type="password"
                       value={props.user.password} onChange={(e) => props.changePassword(e.target.value)}/>
            </div>
            <div>
                <label>Birth date</label>
                <TextInput type="date"
                       value={props.user.birthDate} onChange={(e) => props.changeBirthDate(e.target.value)}/>
            </div>
            <Button onClick={() => props.register(props.user, props.avatar)}>Register</Button>
        </div>
    );
}

export default UserParametersForm;