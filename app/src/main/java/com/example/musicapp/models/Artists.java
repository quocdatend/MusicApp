package com.example.musicapp.models;

public class Artists {
    private int id;
    private String name;
    private String email;
    private String password;
    private String biography;
    private String stageName;
    private String nationality;
    private String debugYear;
    private int albumsCount;
    private String website;
    private int genreId;
    private String avatar;

    public Artists() {
        // Constructor mặc định
        this.id = 0;
        this.name = "";
        this.email = "";
        this.password = "";
        this.biography = "";
        this.stageName = "";
        this.nationality = "";
        this.debugYear = "";
        this.albumsCount = 0;
        this.website = "";
        this.genreId = 0;
        this.avatar = "";
    }

    public Artists(int id, String name, String email, String password, String biography, String stageName, String nationality, String debugYear, int albumsCount, String website, int genreId, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.biography = biography;
        this.stageName = stageName;
        this.nationality = nationality;
        this.debugYear = debugYear;
        this.albumsCount = albumsCount;
        this.website = website;
        this.genreId = genreId;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDebugYear() {
        return debugYear;
    }

    public void setDebugYear(String debugYear) {
        this.debugYear = debugYear;
    }

    public int getAlbumsCount() {
        return albumsCount;
    }

    public void setAlbumsCount(int albumsCount) {
        this.albumsCount = albumsCount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Artists{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", biography='" + biography + '\'' +
                ", stageName='" + stageName + '\'' +
                ", nationality='" + nationality + '\'' +
                ", debugYear='" + debugYear + '\'' +
                ", albumsCount=" + albumsCount +
                ", website='" + website + '\'' +
                ", genreId=" + genreId +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}