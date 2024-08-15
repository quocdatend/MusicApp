package com.example.musicapp.models;

public class Session {
    private int code;
    private String name;
    private String password;
    private String email;
    private int role;

    public Session(int code, String name, String password, String email, int role) {
        this.code = code;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Session(){
        // default
        this.code = 0;
        this.name = "";
        this.password = "";
        this.email = "";
        this.role = 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
