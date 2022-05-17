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
import Group from "./components/pages/group/Group";
import Schedule from "./components/pages/schedule/Schedule";
import UserVideoconferenceRooms from "./components/pages/videoconferencerooms/user/UserVideoconferenceRooms";

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
                            {/*<Route path='/users/:id/groups' element={<UserProfileContainer/>}/> //TODO*/}
                            <Route path='/groups' element={<Groups/>}/>
                            <Route path='/groups/*' element={<Group/>}/>
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
