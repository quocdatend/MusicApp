package com.example.musicapp.models;

public class Session {
    private String name;
    private String password;
    private int role;

    public Session(int role, String password, String name) {
        this.role = role;
        this.password = password;
        this.name = name;
    }

    public Session() {
        this.name = "";
        this.password = "";
        this.role = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
    // clear

}
