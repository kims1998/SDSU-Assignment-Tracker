import "/userService"
import {getUserByEmail} from "./userService";
const API_BASE_URL= 'http://localhost:8080/api';


// Handle Login Checks

export const loginChecks = async (email, password) => {

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password})
        });

        if (!response.ok) {

            const errorData = await response.json();
            throw new Error(errorData.message || 'Login failed: Invalid credentials');
        }

        return await response.json();

    } catch (error) {
        console.error('Error during login:', error);
        throw error;
    }


}