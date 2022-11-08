import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../App';


const MainPage = () => {
    const {getToken} = useContext(AuthContext);
    return (
        <div>
            <div>{getToken()}</div>
            <div><Link to={"/login"}>Login</Link></div>
            <div><Link to={"/forbidden"}>Forbidden</Link></div>
        </div>
    );
};

export default MainPage;