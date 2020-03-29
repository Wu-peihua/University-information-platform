package com.example.uipfrontend.Entity;

public class ResInfo {
    private String portraitUri;
    private String username;
    private String title;
    private String description;
    private String link;
    private String time;

    public ResInfo(String portraitUri, String username, String title, String description, String link, String time) {
        this.portraitUri = portraitUri;
        this.username = username;
        this.title = title;
        this.description = description;
        this.link = link;
        this.time = time;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getTime() {
        return time;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
