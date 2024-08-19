package com.example.musicapp.models;

public class Comment {
    private int id;
    private String title;
    private String timeComment;
    private int likes;
    private int dislikes;
    private int userId;
    private int songId;

    public Comment(int id, String title, String timeComment, int likes, int dislikes, int userId, int songId) {
        this.id = id;
        this.title = title;
        this.timeComment = timeComment;
        this.likes = likes;
        this.dislikes = dislikes;
        this.userId = userId;
        this.songId = songId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeComment() {
        return timeComment;
    }

    public void setTimeComment(String timeComment) {
        this.timeComment = timeComment;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
