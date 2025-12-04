// Base URL for your Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// Always use calendar ID 1 for now (our default calendar)
const CALENDAR_ID = 1;

// ===== CREATE - Add new assignment =====
export const createAssignment = async (assignmentData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/calendar-events`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
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
    try {
        const response = await fetch(
            `${API_BASE_URL}/calendar-events?calendarId=${CALENDAR_ID}`
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
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: assignmentData.title,
                date: assignmentData.date,
                startTime: assignmentData.startTime,
                endTime: assignmentData.endTime,
                eventType: assignmentData.eventType || 'ASSIGNMENT'
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