 // Base URL for your Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// Always use calendar ID 1 for now (our default calendar)
const CALENDAR_ID = 1;

const getAuthHeaders = () => {
    const token = localStorage.getItem('userToken');
    if (!token) {
        console.warn("No authentication token found. User is likely not logged in.");
    }
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : undefined
    }
 }

// ===== CREATE - Add new assignment =====
 export const createAssignment = async (assignmentData) => {
     console.log("Payload for creating assignment:", assignmentData);
     try {
         const body = {
             calendarId: 1,
             eventType: assignmentData.eventType,
             title: assignmentData.title,

             //Required by Backend
             date: assignmentData.date,
             startTime: assignmentData.startTime,
             endTime: assignmentData.endTime,

             //For LocalDateTime Fields
             startDateTime: assignmentData.startDateTime,
             endDateTime: assignmentData.endDateTime,

             priority: assignmentData.priority
         };
         console.log("📤 Sending to backend:", body);

         const response = await fetch(`${API_BASE_URL}/calendar-events`, {
             method: 'POST',
             headers: getAuthHeaders(),
             body: JSON.stringify(body),
         });

         const text = await response.text();
         let data = null;
         if (text) {
             try { data = JSON.parse(text); } catch { data = text }
         }

         console.log("Backend Response:", response.status, data);

         if (!response.ok) {
             const msg = data?.message || `Failed to create assignment ${response.status}`;
             throw new Error(msg);
         }

         return data;
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

        const data = await response.json();

        return data.map(a => ({
            ...a,
            startDateTime: a.startDateTime || `${a.date}T${a.startTime}`,
            endDateTime: a.endDateTime || `${a.date}T${a.endTime}`,
        }));
    } catch (error) {
        console.error('Error fetching assignments:', error);
        throw error;
    }
};

// ===== UPDATE - Edit an assignment =====
export const updateAssignment = async (id, assignmentData) => {
    try {
        const body = {
            calendarId: 1,
            eventType: assignmentData.eventType,
            title: assignmentData.title,

            //Required by Backend
            date: assignmentData.date,
            startTime: assignmentData.startTime,
            endTime: assignmentData.endTime,

            //For LocalDateTime Fields
            startDateTime: assignmentData.startDateTime,
            endDateTime: assignmentData.endDateTime,

            priority: assignmentData.priority
        };
        console.log("📤 Updating:", body);

        const response = await fetch(`${API_BASE_URL}/calendar-events/${id}`, {
            method: 'PUT',
            headers: getAuthHeaders(),
            body: JSON.stringify(body)
        });

        const text = await response.text();
        let data = null;
        if (text) {
            try { data = JSON.parse(text); } catch { data = text }
        }

        if (!response.ok) {
            const msg = data?.message || `Failed to update assignment ${response.status}`;
            throw new Error(msg);
        }
        return data;
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
            headers: getAuthHeaders()
        });

        if (!response.ok) {
            const text = await response.text();
            let msg = `Failed to delete assignment (${response.status})`;
            if (text) {
                try { msg = JSON.parse(text)?.message || msg } catch {}
            }
            throw new Error(msg);
        }

        return true;
    } catch (error) {
        console.error('Error deleting assignment:', error);
        throw error;
    }
};