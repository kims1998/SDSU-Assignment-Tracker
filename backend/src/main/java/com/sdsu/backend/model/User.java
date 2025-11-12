package com.sdsu.backend.model;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email,
                   password,
                   name;
    private boolean activeStatus = false;


    // VVV ||CALENDAR 1-1 || VVV

    // @OneToOne(mappedBy = "calendar")
    // private Calendar calendar;

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // private UserSettings userSettings;

    public User() {
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // getters
    public String getUsername() {
        return name;
    }
    public String getPassword() {return password;}

    public String getEmail() {
        return email;
    }
    public boolean getActiveStatus() {return activeStatus;}
    public Long getId() {return id;}

    // setters

    public void setUsername(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setActiveStatusTrue() {this.activeStatus = true;}
    public void setActiveStatusFalse() {this.activeStatus = false;}

    // public void setUserSettings(UserSettings userSettings){
    // this.userSettings = userSettings;
    // if (userSettings != null){
    // userSettings.setUser(this);
    // }
    // }

}