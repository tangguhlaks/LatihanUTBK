package com.example.tesutbk.model;

import java.io.Serializable;

public class Request implements Serializable {
    private String username;
    private String email;
    private String password;
    private String role;
    private String key;

    public Request(String username, String email, String role, String key) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
