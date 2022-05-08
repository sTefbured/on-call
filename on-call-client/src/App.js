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

const App = () => {
    return (
        <div className={styles.wrapper}>
            <HeaderContainer/>
            <div className={styles.content}>
                <div className={styles.container}>
                    <BrowserRouter>
                        <Routes>
                            <Route path='/' element={<LoginFormContainer/>}/>
                            <Route path='/register' element={<Registration/>}/>
                            <Route path='/users' element={<Users/>}/>
                            <Route path='/conference' element={<VideoConference/>}/>
                            <Route path='/users/:id' element={<UserProfileContainer/>}/>
                            <Route path='/login' element={<LoginFormContainer/>}/>
                        </Routes>
                    </BrowserRouter>
                </div>
            </div>
            <Footer/>
        </div>
    );
}

export default App;
