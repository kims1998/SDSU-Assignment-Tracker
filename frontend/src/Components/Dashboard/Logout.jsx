

import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Logout.css';

const Logout = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('userToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('userEmail');

        navigate('/');
    };

    return (
        <div className="logout-container">
            <button className="logout-button" onClick={handleLogout}>
                Log Out
            </button>
        </div>
    );
};

export default Logout;
