package com.example.a300cem_hkforum;

public class Reply {
    public String content;
    public String timestamp;
    public String user;
    public String ID;
    public Reply() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Reply(String content, String timestamp, String user, String ID) {
        this.content = content;
        this.timestamp = timestamp;
        this.user = user;
        this.ID = ID;
    }

    public String getReplyContent() {return content;}
    public String getReplyTimestamp() {return timestamp;}
    public String getReplyUser() {return user;}
    public String getReplyID(){return ID;}
}
