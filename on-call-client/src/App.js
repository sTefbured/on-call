import React from "react";
import styles from "./app.module.css"
import {BrowserRouter, Route, Routes} from "react-router-dom";
import VideoConference from "./components/pages/videoconference/VideoConference";
import Registration from "./components/pages/registration/Registration";
import Users from "./components/pages/users/Users";
import Footer from "./components/common/footer/Footer";
import UserProfileContainer from "./components/pages/userprofile/UserProfileContainer";
import LoginFormContainer from "./components/pages/login/LoginFormContainer";
import HeaderContainer from "./components/common/header/HeaderContainer";
import Groups from "./components/pages/groups/all/Groups";
import GroupContainer from "./components/pages/group/GroupContainer";
import Schedule from "./components/pages/schedule/Schedule";
import UserVideoconferenceRooms from "./components/pages/videoconferencerooms/user/UserVideoconferenceRooms";
import ChatContainer from "./components/pages/chat/ChatContainer";
import UserChatsContainer from "./components/pages/chats/UserChatsContainer";
import UserGroupsContainer from "./components/pages/groups/usergroups/UserGroupsContainer";

const App = () => {
    return (
        <div className={styles.wrapper}>
            <BrowserRouter>
                <HeaderContainer/>
                <div className={styles.content}>
                    <div className={styles.container}>
                        <Routes>
                            <Route path='/login' element={<LoginFormContainer/>}/>
                            <Route path='/register' element={<Registration/>}/>
                            <Route path='/schedule' element={<Schedule/>}/>
                            <Route path='/conferences' element={<UserVideoconferenceRooms/>}/>
                            <Route path='/conferences/:id' element={<VideoConference/>}/>
                            <Route path='/users' element={<Users/>}/>
                            <Route path='/users/:id' element={<UserProfileContainer/>}/>
                            <Route path='/users/:id/groups' element={<UserGroupsContainer/>}/>
                            <Route path='/groups' element={<Groups/>}/>
                            <Route path='/groups/*' element={<GroupContainer/>}/>
                            <Route path='/chats/:id' element={<ChatContainer/>}/>
                            <Route path='/chats/' element={<UserChatsContainer/>}/>
                            <Route path='/*' element={<LoginFormContainer/>}/>
                        </Routes>
                    </div>
                </div>
                <Footer/>
            </BrowserRouter>
        </div>
    );
}

export default App;
