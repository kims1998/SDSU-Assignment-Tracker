import React, { useState } from 'react'  
import './LoginSignup.css'

import user_icon from '../Assets/person.png'
import email_icon from '../Assets/email.png'
import password_icon from '../Assets/password.png'
import { useNavigate } from 'react-router-dom';

import { loginChecks } from '../../services/loginService';

const LoginSignup = () => {

    const [action, setAction] = useState("Sign Up");
    const navigate = useNavigate();

    // Input states
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    // Navigate to dashboard
    const goToDashboard = () => {
        navigate('/dashboard');
    };

    const handleLogin = async () => {

        console.log("Attempting login for:", email);

        try {
            const loginData = await loginChecks(email, password);

            console.log("Login Successful! Received Data:", loginData);

            localStorage.setItem('userToken', loginData.token);
            localStorage.setItem('userId', loginData.id);
            localStorage.setItem('userEmail', loginData.email);

            alert("Login successful!");

            goToDashboard();

        } catch (error) {
            console.error("Login attempt failed:", error.message);
            alert(error.message);
        }
    }

    // Handle Sign Up
    const handleSignup = async () => {
        // Send only the fields expected by CreateUserRequest
        const userData = {
            name: username,       // matches backend DTO
            email: email,
            password: password
        };

        console.log("Sending user data:", userData);

        try {
            const response = await fetch("http://localhost:8080/api/users", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(userData)
            });

            if (response.ok) {
                alert("Sign up successful!");
                // Optionally reset inputs
                setUsername("");
                setEmail("");
                setPassword("");
            } else {
                const errText = await response.text();
                console.error("Backend error:", errText);
                alert("Sign up failed: " + errText);
            }
        } catch (error) {
            console.error("Network error:", error);
            alert("Network error: " + error.message);
        }
    };

    const handleSubmit = () => {
        if (action === "Sign Up") {
            handleSignup();
        } else {
            handleLogin();
        }
    }

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
                <input 
                    type="text" 
                    placeholder='Name'
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
            </div>}
            <div className='input'>
                <img src={ email_icon } alt="" />
                <input 
                    type="email" 
                    placeholder='Email ID'
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
            </div>
            <div className='input'>
                <img src={ password_icon } alt="" />
                <input 
                    type="password" 
                    placeholder='Password'
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </div>
            {action==="Sign Up"?<div></div>:<div className="forgot-password">Forgot Password? <span>Click Here</span></div>}
        </div>

        <div className="submit-container">
            <div 
                className={ action==="Login"?"submit gray": "submit" }
                onClick={ action === "Sign Up" ? () => handleSubmit() : () => setAction("Sign Up") }  // Sign Up sends data
            >
                Sign Up
            </div>
            {/* Login Button: Call handleSubmit if active, or switches view if inactive */}
            <div 
                className={ action==="Sign Up"?"submit gray":"submit" } 
                onClick={ action === "Login" ? () => handleSubmit() : () => setAction("Login") }  // Switch to login view
            >
                Login
            </div>
        </div>

        {/* Dashboard navigation button */}
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

export default LoginSignup;