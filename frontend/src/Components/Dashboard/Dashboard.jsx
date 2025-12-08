import React, { useState, useEffect, useCallback } from 'react';
import './Dashboard.css';
import {
    createAssignment,
    getAllAssignments,
    updateAssignment,
    deleteAssignment
} from '../../services/calendarEventService';

function Dashboard() {
    //setState is not used, instead use updateState (But don't delete setState)
    const [state, setState] = useState({
        assignments: [],
        filteredAssignments: [],
        currentFilter: 'ALL',
        loading: false,
        error: null,
        showModal: false,
        editingId: null,
        currentCalendarDate: new Date(),
        formData: {
            eventType: 'ASSIGNMENT',
            title: '',
            date: '',
            startTime: '9',
            endTime: '10',
        }
    });

    const updateState = (updates) =>
        setState(prev => ({
            ...prev,
            ...updates
        })
    );

    const fetchAssignments = useCallback(async () => {
        updateState ({
            loading: true,
            error: null
        });
        try {
            const data = await getAllAssignments();
            const assignmentsWithEpoch = data.map(a => ({
                ...a,
                eventType: a.eventType,
                epochDate: Math.floor(new Date(a.date).getTime() / 86400000),
                epochStart: new Date(a.startDateTime).getTime(),
                epochEnd: new Date(a.endDateTime).getTime()
            }))
            updateState ({ assignments: assignmentsWithEpoch });
        } catch (err) {
            updateState ({ error: 'Failed to load assignments. Make sure backend is running!' });
            console.error(err);
        } finally {
            updateState ({ loading: false });
        }
    }, []);

    // Fetch assignments on load
    useEffect(() => {
        fetchAssignments().catch(err =>
            console.error(err));
    }, [fetchAssignments]);

    const filterAssignments = useCallback(() => {
        if (state.currentFilter === 'ALL') {
            updateState({ filteredAssignments: state.assignments });
            return;
        }

        updateState({
            filteredAssignments: state.assignments.filter(a =>
                a.eventType === state.currentFilter)
        });
    }, [state.currentFilter, state.assignments]);

    // Update filtered assignments when filter or assignments change
    useEffect(() => {
        filterAssignments();
    }, [filterAssignments]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        updateState ({
            formData: {
                ...state.formData,
                [name]: value
            }
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        updateState ({ error: null });

        if (!state.formData.title || !state.formData.date || !state.formData.startTime, !state.formData.endTime) {
            updateState ({ error: 'Title and date are required!' });
            return;
        }

        try {
            const priorityMap = {
                SPECIAL_EVENT: 1,
                ASSIGNMENT: 2,
                SCHOOL_CLASS: 3
            };

            const startDateTime = `${state.formData.date}T${state.formData.startTime}`;
            const endDateTime = `${state.formData.endTime}T${state.formData.endTime}`;

            const data = {
                ...state.formData,
                startDateTime,
                endDateTime,
                priority: priorityMap[state.formData.priority]
            };

            if (state.editingId) {
                await updateAssignment(state.editingId, data);
            } else {
                await createAssignment(data);
            }
            closeModal();
            await fetchAssignments();
        } catch (err) {
            updateState ({ error: 'Failed to save assignment!' });
            console.error(err);
        }
    };

    const handleEdit = (assignment) => {
        updateState ({
            formData: {
                eventType: assignment.eventType,
                title: assignment.title,
                date: assignment.startDateTime(0, 10), //YYYY-MM-DD
                startTime: assignment.startDateTime.slice(11,16), //HH:MM
                endTime: assignment.endDateTime.slice(11, 16)
            },
            editingId: assignment.id,
            showModal: true
        });
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this assignment?')) {
            try {
                await deleteAssignment(id);
                await fetchAssignments();
            } catch (err) {
                updateState ({ error: 'Failed to delete assignment!' });
                console.error(err);
            }
        }
    };

    const closeModal = () => {
        updateState ({
            showModal: false,
            editingId: null,
            formData: ({
                eventType: 'ASSIGNMENT',
                title: '',
                date: '',
                startTime: '9',
                endTime: '10'
            })
        });
    };

    const showCreateModal = () => {
        updateState ({
            editingId: null,
            formData: {
                eventType: 'ASSIGNMENT',
                title: '',
                date: '',
                startTime: '9',
                endTime: '10'
            },
            showModal: true
        });
    };

    const formatTime = (dateTimeStr) => {
        const dt = new Date(dateTimeStr);
        const hr = dt.getHours().toString().padStart(2, '0');
        const min = dt.getMinutes().toString().padStart(2, '0');
        return `${hr}:${min}`;
    };

    const getTypeLabel = (eventType) => {
        const labels = {
            "SPECIAL_EVENT": '⭐ Special Event',
            "ASSIGNMENT": '📚 Assignment',
            "SCHOOL_CLASS": '🎓 Class'
        };
        return labels[eventType] || 'Unknown';
    };

    const getTypeClass = (eventType) => {
        const classes = {
            "SPECIAL_EVENT": 'special-event',
            "ASSIGNMENT": 'assignment',
            "SCHOOL_CLASS": 'school-class'
        };
        return classes[eventType] || '';
    };

    // Calendar functions
    const renderCalendar = () => {
        const year = state.currentCalendarDate.getFullYear();
        const month = state.currentCalendarDate.getMonth();

        const monthNames = [
            'January', 'February', 'March', 'April', 'May', 'June',
            'July', 'August', 'September', 'October', 'November', 'December'
        ];

        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);
        const daysInMonth = lastDay.getDate();
        const startingDayOfWeek = firstDay.getDay();
        const prevMonthLastDay = new Date(year, month, 0).getDate();

        const days = [];

        // Day headers
        const dayHeaders = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
        dayHeaders.forEach(day => {
            days.push(
                <div key={ `header-${day}` } className="calendar-day-header">
                    { day }
                </div>
            );
        });

        // Previous month's days
        for (let i = startingDayOfWeek - 1; i >= 0; i--) {
            const dayNum = prevMonthLastDay - i;
            days.push(
                <div key={ `prev-${ dayNum }` } className="calendar-day other-month">
                    <div className="calendar-day-number">{ dayNum }</div>
                </div>
            );
        }

        // Current month's days
        const today = new Date();
        for (let day = 1; day <= daysInMonth; day++) {
            const currentDate = new Date(year, month, day);
            const isToday =
                day === today.getDate() &&
                month === today.getMonth() &&
                year === today.getFullYear();

            const epochDate = Math.floor(currentDate.getTime() / 86400000);
            const dayEvents = state.assignments.filter(a => a.epochDate === epochDate);

            days.push(
                <div
                    key={ `current-${ day }` }
                    className={ `calendar-day ${ isToday ? 'today' : '' }` }
                >
                    <div className="calendar-day-number">{ day }</div>
                    {dayEvents.slice(0, 3).map(event => (
                        <div
                            key={ event.id }
                            className={ `calendar-event-dot ${ getTypeClass(event.eventType) }` }
                            title={ `${ event.title }(${ formatTime(event.startTime) } - ${ formatTime(event.endTime) })` }
                            onClick={(e) => {
                                e.stopPropagation();
                                handleEdit(event);
                            }}
                        >
                            { event.title }
                        </div>
                    ))}
                    {dayEvents.length > 3 && (
                        <div className="calendar-event-count">+{ dayEvents.length - 3 } more</div>
                    )}
                </div>
            );
        }

        // Next month's days
        const totalCells = days.length - 7;
        const remainingCells = 42 - totalCells;
        for (let day = 1; day <= remainingCells; day++) {
            days.push(
                <div key={ `next-${ day }` } className="calendar-day other-month">
                    <div className="calendar-day-number">{ day }</div>
                </div>
            );
        }
        return {
            title: `📅 ${monthNames[month]} ${year}`,
            days
        };
    };

    const previousMonth = () => {
        updateState ({
            currentCalendarDate: new Date(state.currentCalendarDate.setMonth(state.currentCalendarDate.getMonth() - 1, 1))
        });
    };

    const nextMonth = () => {
        updateState ({
            currentCalendarDate: new Date(state.currentCalendarDate.setMonth(state.currentCalendarDate.getMonth() + 1, 1))
        });
    };

    const goToToday = () => {
        updateState ({
            currentCalendarDate: new Date()
        });
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
                <button className="btn" onClick={ showCreateModal }>➕ Add New Event</button>
                <button className="btn" onClick={ fetchAssignments }>🔄 Refresh</button>

                <div className="filter-buttons">
                    <strong>Filter by type:</strong>
                    <button
                        className={ `filter-btn ${ state.currentFilter === 'ALL' ? 'active' : '' }` }
                        onClick={() => updateState ({
                            currentFilter: 'ALL'
                        })}
                    >
                        All
                    </button>
                    <button
                        className={ `filter-btn ${ state.currentFilter === 'ASSIGNMENT' ? 'active' : '' }` }
                        onClick={() => updateState ({
                            currentFilter: 'ASSIGNMENT'
                        })}
                    >
                        📚 Assignments
                    </button>
                    <button
                        className={ `filter-btn ${ state.currentFilter === 'SCHOOL_CLASS' ? 'active' : '' }` }
                        onClick={() => updateState ({
                            currentFilter: 'SCHOOL_CLASS'
                        })}
                    >
                        🎓 Classes
                    </button>
                    <button
                        className={ `filter-btn ${ state.currentFilter === 'SPECIAL_EVENT' ? 'active' : '' }` }
                        onClick={() => updateState ({
                            currentFilter: 'SPECIAL_EVENT'
                        })}
                    >
                        ⭐ Special Events
                    </button>
                </div>
            </div>

            {/* Error Message */}
            { state.error && <div className="error-message">{ state.error }</div> }

            {/* Events Grid */}
            <div className="events-grid">
                { state.loading && <p>Loading assignments...</p> }

                {!state.loading && state.filteredAssignments.length === 0 && (
                    <div className="empty-state">
                        <div className="empty-state-icon">📚</div>
                        <h3>No events found</h3>
                        <p>Click "Add New Event" to create your first event!</p>
                    </div>
                )}

                {!state.loading && state.filteredAssignments.map(assignment => (
                    <div key={ assignment.id } className={ `event-card ${getTypeClass(assignment.eventType)}` }>
                        <div className="event-header">
                            <div>
                                <div className="event-title">
                                    { assignment.title }
                                </div>
                                <span className={ `event-type type-${getTypeClass(assignment.eventType)}` }>
                                    { getTypeLabel(assignment.eventType) }
                                </span>
                            </div>
                        </div>
                        <div className="event-details">
                            📅 { assignment.date } | ⏰ { formatTime(assignment.startTime) } - { formatTime(assignment.endTime) }
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
                    <h2>{ calendar.title }</h2>
                    <div className="calendar-nav">
                        <button onClick={ previousMonth }>← Prev</button>
                        <button onClick={ goToToday }>Today</button>
                        <button onClick={ nextMonth }>Next →</button>
                    </div>
                </div>
                <div className="calendar-grid">
                    { calendar.days }
                </div>
            </div>

{/* Modal */}
{ state.showModal && (
    <div className="modal active">
        <div className="modal-content">
            <h2>{ state.editingId ? 'Edit Event' : 'Create New Event' }</h2>
            <form onSubmit={ handleSubmit }>
                {/* ADD THIS EVENT TYPE SELECTOR */}
                <div className="form-group">
                    <label>Event Type *</label>
                    <select
                        name="eventType"
                        value={ state.formData.eventType }
                        onChange={ handleInputChange }
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
                        value={ state.formData.title }
                        onChange={ handleInputChange }
                        placeholder="CS250 Final Project"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Date *</label>
                    <input
                        type="date"
                        name="date"
                        value={ state.formData.date }
                        onChange={ handleInputChange }
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Start Time *</label>
                    <input
                        type="time"
                        name="startTime"
                        value={ state.formData.startTime }
                        onChange={ handleInputChange }
                        min="00:00"
                        max="23:59"
                        step="60"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>End Time *</label>
                    <input
                        type="time"
                        name="endTime"
                        value={ state.formData.endTime }
                        onChange={ handleInputChange }
                        required
                    />
                </div>

                <div className="form-group">
                    <button type="submit" className="btn">💾 Save Event</button>
                    <button type="button" className="btn" onClick={ closeModal }>❌ Cancel</button>
                </div>
            </form>
        </div>
    </div>
)}
        </div>
    );
}

export default Dashboard;