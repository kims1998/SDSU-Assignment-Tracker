package com.sdsu.backend.model;

import jakarta.persistance.*;



@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

    @OneToOne(mapped by = "calendar")
    private Calendar calendar;

    public User(){}

    public User(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public void setPassword(){
        
    }

    public void setEmail(){

    }


}