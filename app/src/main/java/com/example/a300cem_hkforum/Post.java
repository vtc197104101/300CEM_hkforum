package com.example.a300cem_hkforum;

public class Post {
    public String title;
    public String content;
    public String timestamp;
    public String user;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String title, String content, String timestamp, String user) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.user = user;
    }

    public String getTitle() {return title;}
    public String getContent() {return content;}
    public String getTimestamp() {return timestamp;}
    public String getUser() {return user;}
}
