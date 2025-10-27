package com.sdsu.backend.model;
import jakarta.persistence.*;

@Entity
public class UserSettings extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean darkMode;
    private boolean notifications;

    @OneToOne
    // @JoinColumn(name = "user_id", referencedColumn = "id")
    private User user;

    public UserSettings(){}

    public UserSettings(User user, boolean darkMode, boolean notifications){
        this.user = user;
        this.darkMode = darkMode;
        this.notifications = notifications;
    }

    public Long getId(){
        return id;
    }

    public boolean isDarkMode(){
        return darkMode;
    }

    public void setDarkMode(boolean darkMode){
        this.darkMode = darkMode;
    }

    public boolean isNotifications(){
        return notifications;
    }

    public void setNotifications(boolean notifications){
        this.notifications = notifications;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
    
}