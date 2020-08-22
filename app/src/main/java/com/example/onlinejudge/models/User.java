package com.example.onlinejudge.models;

import java.util.Date;

public class User {
    protected int id;
    protected String username;
    protected String email;
    protected Date timeRegistered;
    protected String token;

    public User() {}

    public User(int id, String username, String email, Date timeRegistered, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.timeRegistered = timeRegistered;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getTimeRegistered() {
        return timeRegistered;
    }

    public void setTimeRegistered(Date timeRegistered) {
        this.timeRegistered = timeRegistered;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", timeRegistered=" + timeRegistered +
                ", token='" + token + '\'' +
                '}';
    }
}
