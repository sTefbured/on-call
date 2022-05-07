import React from "react";
import styles from "./app.module.css"
import {BrowserRouter, Route, Routes} from "react-router-dom";
import VideoConference from "./components/pages/videoconference/VideoConference";
import Registration from "./components/pages/registration/Registration";
import Users from "./components/pages/users/Users";
import Header from "./components/common/header/Header";
import Footer from "./components/common/footer/Footer";
import UserProfileContainer from "./components/pages/userprofile/UserProfileContainer";
import LoginFormContainer from "./components/pages/login/LoginFormContainer";

const App = () => {
    return (
        <div className={styles.wrapper}>
            <Header/>
            <div className={styles.content}>
                <div className={styles.container}>
                    <BrowserRouter>
                        <Routes>
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
