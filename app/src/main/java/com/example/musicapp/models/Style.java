package com.example.musicapp.models;

public class Style {
    private int id;
    private String Name;
    private String Detail;

    public Style(int id, String name, String detail) {
        this.id = id;
        Name = name;
        Detail = detail;
    }

    public Style() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    @Override
    public String toString() {
        return "Style{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", Detail='" + Detail + '\'' +
                '}';
    }
}
