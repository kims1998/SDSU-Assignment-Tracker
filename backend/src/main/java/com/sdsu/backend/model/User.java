package com.sdsu.backend.model;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

    // VVV ||CALENDAR 1-1 || VVV

    // @OneToOne(mappedBy = "calendar")
    // private Calendar calendar;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserSettings userSettings;

    public User(){}

    public User(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getUsername(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public void setUsername(String name){
        this.name = name;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setUserSettings(UserSettings userSettings){
        this.userSettings = userSettings;
        if (userSettings != null){
            userSettings.setUser(this);
        }
    }

}