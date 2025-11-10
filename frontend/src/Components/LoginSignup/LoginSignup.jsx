import React, { useState } from 'react'  
import './LoginSignup.css'

import user_icon from '../Assets/person.png'
import email_icon from '../Assets/email.png'
import password_icon from '../Assets/password.png'
import { useNavigate } from 'react-router-dom';

const LoginSignup = () => {

    const [action, setAction] = useState("Sign Up");
    // ADDED THIS 21OCT2025 TO NAGIVATE FROM / -> /LOGIN -> /DASHBOARD
    const navigate = useNavigate()

    // 21OCT2025 ADDED NAVIAGTE FUNCION
        // ADD THIS FUNCTION
    const goToDashboard = () => {
        navigate('/dashboard');
    };

  return (
    <div className='container'>
        <div className='header'>
            <div className='text-sdsu'> <span role="img" aria-label="dog">📚</span>SDSU Assignment Tracker</div>
            <div className='text'> { action } </div>
            <div className='underline'></div>
        </div>

        <div className='inputs'>
            {action==="Login"?<div></div>:<div className='input'>
                <img src={ user_icon } alt="" />
                <input type="text" placeholder='Name'/>
            </div>}
            <div className='input'>
                <img src={ email_icon } alt="" />
                <input type="email" placeholder='Email ID'/>
            </div>
            <div className='input'>
                <img src={ password_icon } alt="" />
                <input type="password" placeholder='Password'/>
            </div>
            {action==="Sign Up"?<div></div>:<div className="forgot-password">Forgot Password? <span>Click Here</span></div>
        }
        </div>
        <div className="submit-container">
            <div className={ action==="Login"?"submit gray": "submit" } onClick={() => {setAction("Sign Up")} }>Sign Up</div>
            <div className={ action==="Sign Up"?"submit gray":"submit" } onClick={() => {setAction("Login")} }>Login</div>
        </div>

        {/*21OCT2025 ADDED BUTTON */}
                    {/* ADD THIS BUTTON */}
            <div style={ {marginTop: '20px', textAlign: 'center'} }>
                <button 
                    onClick={ goToDashboard }
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#d41736',
                        color: 'white',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontSize: '16px'
                    }}
                >
                    Go to Dashboard (Skip Login)
                </button>
            </div>
        </div>
    );
}

export default LoginSignup
