// Base URL for your Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// ===== CREATE A NEW USER =====

export const createUser = async (userData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: userData.email,
                password: userData.password,
                name: userData.name,
            })
        });

        if (!response.ok) {
            throw new Error('Failed to create user');
        }

        return await response.json();
    } catch (error) {
        console.error('Error creating assignment:', error);
        throw error;
    }
};

// ===== READ - Get User's email/password/username =====

// Get All the Users
export const getAllActiveUsers = async () => {
    try {
        const response = await fetch(
            `${API_BASE_URL}/users?activeStatus=true`
        );

        if (!response.ok) {
            throw new Error('Failed to fetch all Active Users');
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching Active Users:', error);
        throw error;
    }
}

// Get User via Email

export const getUserByEmail = async (email) => {
    try {
        const response = await fetch(
            `${API_BASE_URL}/users/email/${email}`
        );

        if (!response.ok) {
            throw new Error('Failed to fetch a User with this EMAIL');
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching User via this EMAIL', error);
        throw error;
    }
}

// Get User via Password
export const getUserByPassword = async (password) => {
    try {
        const response = await fetch(
            `${API_BASE_URL}/users/password/${password}`
        );

        if (!response.ok) {
            throw new Error('Failed to fetch a User with this PASSWORD');
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching User via this PASSWORD', error)
    }
}

// Get User via Name
export const getUserByName = async (name) => {
    try {
        const response = await fetch(
            `${API_BASE_URL}/users/name/${name}`
        );

        if (!response.ok) {
            throw new Error('Failed to fetch a User with this NAME');
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching User via this NAME', error)
    }
}


// ===== UPDATE - Edit User's information =====
// Update via Email
export const updateUser = async (email, userData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/users/email/${email}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: userData.email,
                password: userData.password,
                name: userData.name,
            }),
        });

        if (!response.ok) {
            throw new Error('Failed to update this user');
        }

        return await response.json();
    } catch (error) {
        console.error('Error updating USER', error);
        throw error;
    }
}

// ===== DELETE - Remove A User =====
// Delete a User via Email
/*
export const deleteUser = async (email) => {
    try {
        const response = await fetch(`${API_BASE_URL}/users/email/${email}`, {
            method: 'DELETE'
        })
    }
}

 */



