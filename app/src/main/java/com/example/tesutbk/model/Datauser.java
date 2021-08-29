package com.example.tesutbk.model;

public class Datauser {
    private String key;
    private String email;
    private String username;

    public Datauser(){

    }
    public Datauser(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
