import {useParams} from "react-router-dom";
import {useEffect} from "react";

const UserProfile = (props) => {
    let params = useParams();
    useEffect(() => props.getUserById(params.id), [params]);
    return !props.user
        ? <></>
        : (
        <div>
            <div>{props.user.id}</div>
            <div>{props.user.username}</div>
            <div>{props.user.email}</div>
            <div>{props.user.firstName}</div>
            <div>{props.user.lastName}</div>
            <div>{props.user.birthDate}</div>
            <div>{props.user.registrationDateTime}</div>
            <div>{props.user.lastVisitDateTime}</div>
            <div>{props.user.passwordExpirationDate}</div>
            <div>{props.user.isBanned}</div>
            <div>{props.user.isEnabled}</div>
            <div>{props.user.avatarUrl}</div>
        </div>
    )
}

export default UserProfile;