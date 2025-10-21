import React, { useState, useEffect } from 'react';
import './Dashboard.css';
import {
    createAssignment,
    getAllAssignments,
    updateAssignment,
    deleteAssignment,
} from '../../services/calendarEventService';

function Dashboard() {
    const [assignments, setAssignments] = useState([]);
    const [filteredAssignments, setFilteredAssignments] = useState([]);
    const [currentFilter, setCurrentFilter] = useState('ALL');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [editingId, setEditingId] = useState(null);
    const [currentCalendarDate, setCurrentCalendarDate] = useState(new Date());

    const [formData, setFormData] = useState({
        eventType: 'ASSIGNMENT',
        title: '',
        date: '',
        startTime: '9',
        endTime: '10',
    });

    // Fetch assignments on load
    useEffect(() => {
        fetchAssignments();
    }, []);

    // Update filtered assignments when filter or assignments change
    useEffect(() => {
        filterAssignments();
    }, [currentFilter, assignments]);

    const fetchAssignments = async () => {
        setLoading(true);
        setError(null);
        try {
            const data = await getAllAssignments();
            setAssignments(data);
        } catch (err) {
            setError('Failed to load assignments. Make sure backend is running!');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const filterAssignments = () => {
        if (currentFilter === 'ALL') {
            setFilteredAssignments(assignments);
        } else {
            // Filter by priority (since we don't have type yet)
            // Priority 1 = Special Event, 2 = Assignment, 3 = Class
            let priorityFilter;
            if (currentFilter === 'SPECIAL_EVENT') priorityFilter = 1;
            if (currentFilter === 'ASSIGNMENT') priorityFilter = 2;
            if (currentFilter === 'SCHOOL_CLASS') priorityFilter = 3;
            
            setFilteredAssignments(
                assignments.filter(a => a.priority === priorityFilter)
            );
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        if (!formData.title || !formData.date) {
            setError('Title and date are required!');
            return;
        }

        try {
            const data = {
                ...formData,
                startTime: parseFloat(formData.startTime),
                endTime: parseFloat(formData.endTime),
            };

            if (editingId) {
                await updateAssignment(editingId, data);
            } else {
                await createAssignment(data);
            }

            closeModal();
            fetchAssignments();
        } catch (err) {
            setError('Failed to save assignment!');
            console.error(err);
        }
    };

    const handleEdit = (assignment) => {
        setFormData({
            title: assignment.title,
            date: assignment.date,
            startTime: assignment.startTime.toString(),
            endTime: assignment.endTime.toString(),
        });
        setEditingId(assignment.id);
        setShowModal(true);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this assignment?')) {
            try {
                await deleteAssignment(id);
                fetchAssignments();
            } catch (err) {
                setError('Failed to delete assignment!');
                console.error(err);
            }
        }
    };

    const closeModal = () => {
        setShowModal(false);
        setEditingId(null);
        setFormData({ eventType: 'ASSIGNMENT', title: '', date: '', startTime: '9', endTime: '10' });
    };

    const showCreateModal = () => {
        setEditingId(null);
        setFormData({ eventType: 'ASSIGNMENT', title: '', date: '', startTime: '9', endTime: '10' });
        setShowModal(true);
    };

    const formatTime = (hour) => {
        return `${hour.toString().padStart(2, '0')}:00`;
    };

    const getTypeLabel = (priority) => {
        const labels = {
            1: '⭐ Special Event',
            2: '📚 Assignment',
            3: '🎓 Class'
        };
        return labels[priority] || 'Unknown';
    };

    const getTypeClass = (priority) => {
        const classes = {
            1: 'special-event',
            2: 'assignment',
            3: 'school-class'
        };
        return classes[priority] || '';
    };

    const getPriorityLabel = (priority) => {
        const labels = { 1: 'High', 2: 'Medium', 3: 'Normal' };
        return labels[priority] || priority;
    };

    // Calendar functions
    const renderCalendar = () => {
        const year = currentCalendarDate.getFullYear();
        const month = currentCalendarDate.getMonth();
        
        const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
                           'July', 'August', 'September', 'October', 'November', 'December'];
        
        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);
        const daysInMonth = lastDay.getDate();
        const startingDayOfWeek = firstDay.getDay();
        const prevMonthLastDay = new Date(year, month, 0).getDate();

        const days = [];
        const dayHeaders = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

        // Day headers
        dayHeaders.forEach(day => {
            days.push(<div key={`header-${day}`} className="calendar-day-header">{day}</div>);
        });

        // Previous month's days
        for (let i = startingDayOfWeek - 1; i >= 0; i--) {
            const dayNum = prevMonthLastDay - i;
            days.push(
                <div key={`prev-${dayNum}`} className="calendar-day other-month">
                    <div className="calendar-day-number">{dayNum}</div>
                </div>
            );
        }

        // Current month's days
        const today = new Date();
        for (let day = 1; day <= daysInMonth; day++) {
            const isToday = day === today.getDate() && 
                           month === today.getMonth() && 
                           year === today.getFullYear();
            
            const date = new Date(year, month, day);
            const epochDate = Math.floor(date.getTime() / 86400000);
            const dayEvents = assignments.filter(a => a.epochDate === epochDate);

            days.push(
                <div 
                    key={`current-${day}`} 
                    className={`calendar-day ${isToday ? 'today' : ''}`}
                >
                    <div className="calendar-day-number">{day}</div>
                    {dayEvents.slice(0, 3).map(event => (
                        <div 
                            key={event.id}
                            className={`calendar-event-dot ${getTypeClass(event.priority)}`}
                            title={`${event.title} (${formatTime(event.startTime)} - ${formatTime(event.endTime)})`}
                            onClick={(e) => {
                                e.stopPropagation();
                                handleEdit(event);
                            }}
                        >
                            {event.title}
                        </div>
                    ))}
                    {dayEvents.length > 3 && (
                        <div className="calendar-event-count">+{dayEvents.length - 3} more</div>
                    )}
                </div>
            );
        }

        // Next month's days
        const totalCells = days.length - 7;
        const remainingCells = 42 - totalCells;
        for (let day = 1; day <= remainingCells; day++) {
            days.push(
                <div key={`next-${day}`} className="calendar-day other-month">
                    <div className="calendar-day-number">{day}</div>
                </div>
            );
        }

        return {
            title: `📅 ${monthNames[month]} ${year}`,
            days
        };
    };

    const previousMonth = () => {
        setCurrentCalendarDate(new Date(currentCalendarDate.setMonth(currentCalendarDate.getMonth() - 1)));
    };

    const nextMonth = () => {
        setCurrentCalendarDate(new Date(currentCalendarDate.setMonth(currentCalendarDate.getMonth() + 1)));
    };

    const goToToday = () => {
        setCurrentCalendarDate(new Date());
    };

    const calendar = renderCalendar();

    return (
        <div className="dashboard-container">
            {/* Header */}
            <header className="dashboard-header-new">
                <h1>🎓 SDSU Assignment Tracker</h1>
                <p className="calendar-id">Calendar ID: 1</p>
            </header>

            {/* Controls */}
            <div className="controls">
                <button className="btn" onClick={showCreateModal}>➕ Add New Event</button>
                <button className="btn" onClick={fetchAssignments}>🔄 Refresh</button>
                
                <div className="filter-buttons">
                    <strong>Filter by type:</strong>
                    <button 
                        className={`filter-btn ${currentFilter === 'ALL' ? 'active' : ''}`}
                        onClick={() => setCurrentFilter('ALL')}
                    >
                        All
                    </button>
                    <button 
                        className={`filter-btn ${currentFilter === 'ASSIGNMENT' ? 'active' : ''}`}
                        onClick={() => setCurrentFilter('ASSIGNMENT')}
                    >
                        📚 Assignments
                    </button>
                    <button 
                        className={`filter-btn ${currentFilter === 'SCHOOL_CLASS' ? 'active' : ''}`}
                        onClick={() => setCurrentFilter('SCHOOL_CLASS')}
                    >
                        🎓 Classes
                    </button>
                    <button 
                        className={`filter-btn ${currentFilter === 'SPECIAL_EVENT' ? 'active' : ''}`}
                        onClick={() => setCurrentFilter('SPECIAL_EVENT')}
                    >
                        ⭐ Special Events
                    </button>
                </div>
            </div>

            {/* Error Message */}
            {error && <div className="error-message">{error}</div>}

            {/* Events Grid */}
            <div className="events-grid">
                {loading && <p>Loading assignments...</p>}

                {!loading && filteredAssignments.length === 0 && (
                    <div className="empty-state">
                        <div className="empty-state-icon">📚</div>
                        <h3>No events found</h3>
                        <p>Click "Add New Event" to create your first event!</p>
                    </div>
                )}

                {!loading && filteredAssignments.map(assignment => (
                    <div key={assignment.id} className={`event-card ${getTypeClass(assignment.priority)}`}>
                        <div className="event-header">
                            <div>
                                <div className="event-title">
                                    {assignment.title}
                                    <span className={`priority-badge priority-${assignment.priority}`}>
                                        Priority: {getPriorityLabel(assignment.priority)}
                                    </span>
                                </div>
                                <span className={`event-type type-${getTypeClass(assignment.priority)}`}>
                                    {getTypeLabel(assignment.priority)}
                                </span>
                            </div>
                        </div>
                        <div className="event-details">
                            📅 {assignment.date} | ⏰ {formatTime(assignment.startTime)} - {formatTime(assignment.endTime)}
                        </div>
                        <div className="event-actions">
                            <button className="btn btn-small" onClick={() => handleEdit(assignment)}>
                                ✏️ Edit
                            </button>
                            <button className="btn btn-delete btn-small" onClick={() => handleDelete(assignment.id)}>
                                🗑️ Delete
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            {/* Calendar View */}
            <div className="calendar-container">
                <div className="calendar-header">
                    <h2>{calendar.title}</h2>
                    <div className="calendar-nav">
                        <button onClick={previousMonth}>← Prev</button>
                        <button onClick={goToToday}>Today</button>
                        <button onClick={nextMonth}>Next →</button>
                    </div>
                </div>
                <div className="calendar-grid">
                    {calendar.days}
                </div>
            </div>

{/* Modal */}
{showModal && (
    <div className="modal active">
        <div className="modal-content">
            <h2>{editingId ? 'Edit Event' : 'Create New Event'}</h2>
            <form onSubmit={handleSubmit}>
                {/* ADD THIS EVENT TYPE SELECTOR */}
                <div className="form-group">
                    <label>Event Type *</label>
                    <select
                        name="eventType"
                        value={formData.eventType}
                        onChange={handleInputChange}
                        required
                    >
                        <option value="ASSIGNMENT">📚 Assignment</option>
                        <option value="SCHOOL_CLASS">🎓 Class</option>
                        <option value="SPECIAL_EVENT">⭐ Special Event</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>Title *</label>
                    <input
                        type="text"
                        name="title"
                        value={formData.title}
                        onChange={handleInputChange}
                        placeholder="CS250 Final Project"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Date *</label>
                    <input
                        type="date"
                        name="date"
                        value={formData.date}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Start Time (hour 0-23) *</label>
                    <input
                        type="number"
                        name="startTime"
                        value={formData.startTime}
                        onChange={handleInputChange}
                        min="0"
                        max="23"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>End Time (hour 0-23) *</label>
                    <input
                        type="number"
                        name="endTime"
                        value={formData.endTime}
                        onChange={handleInputChange}
                        min="0"
                        max="23"
                        required
                    />
                </div>

                <div className="form-group">
                    <button type="submit" className="btn">💾 Save Event</button>
                    <button type="button" className="btn" onClick={closeModal}>❌ Cancel</button>
                </div>
            </form>
        </div>
    </div>
)}
        </div>
    );
}

export default Dashboard;