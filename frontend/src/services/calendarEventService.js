 // Base URL for your Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// Always use calendar ID 1 for now (our default calendar)
const CALENDAR_ID = 1;

const getAuthHeaders = () => {
    const token = localStorage.getItem('userToken');
    if (!token) {
        throw new Error("User not authenicated. Please log in.");
    }
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };
}

// ===== CREATE - Add new assignment =====
export const createAssignment = async (assignmentData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/calendar-events`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({
                calendarId: CALENDAR_ID,
                eventType: assignmentData.eventType || 'ASSIGNMENT',  // ADD THIS
                title: assignmentData.title,
                date: assignmentData.date,
                startTime: assignmentData.startTime || 0,
                endTime: assignmentData.endTime || 0,
            }),
        });

        if (!response.ok) {
            throw new Error('Failed to create assignment');
        }

        return await response.json();
    } catch (error) {
        console.error('Error creating assignment:', error);
        throw error;
    }
};

// ===== READ - Get all assignments =====
export const getAllAssignments = async () => {
    const token = localStorage.getItem('userToken');

    if (!token) {
        // Handle case where user is not logged in (e.g., redirect to login)
        console.warn("No authentication token found. Cannot load assignments.");
        // Depending on your frontend flow, you might throw an error or return an empty array
        return [];
    }

    try {
        const response = await fetch(
            `${API_BASE_URL}/calendar-events?calendarId=${CALENDAR_ID}`,
            {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
            }
        );

        if (!response.ok) {
            throw new Error('Failed to fetch assignments');
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching assignments:', error);
        throw error;
    }
};

// ===== UPDATE - Edit an assignment =====
export const updateAssignment = async (id, assignmentData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/calendar-events/${id}`, {
            method: 'PUT',
            headers: getAuthHeaders(),
            body: JSON.stringify({
                calendarId: CALENDAR_ID,
                title: assignmentData.title,
                date: assignmentData.date,
                startTime: assignmentData.startTime,
                endTime: assignmentData.endTime,
                eventType: assignmentData.eventType || 'ASSIGNMENT',
                priority: assignmentData.priority
            }),
        });

        if (!response.ok) {
            throw new Error('Failed to update assignment');
        }

        return await response.json();
    } catch (error) {
        console.error('Error updating assignment:', error);
        throw error;
    }
};

// ===== DELETE - Remove an assignment =====
export const deleteAssignment = async (id) => {
    try {
        const response = await fetch(`${API_BASE_URL}/calendar-events/${id}`, {
            method: 'DELETE',
            headers: getAuthHeaders(),
        });

        if (!response.ok) {
            throw new Error('Failed to delete assignment');
        }

        return true;
    } catch (error) {
        console.error('Error deleting assignment:', error);
        throw error;
    }
};