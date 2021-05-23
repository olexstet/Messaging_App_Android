package com.example.messaging_app;

public class Message {
    private String body_message; // message body
    private String name_user; // data of the user that sent this message
    private boolean currentUser; // is this message sent by us?

    public Message(String body, String username, boolean currentUser) {
        this.body_message = body;
        this.name_user = username;
        this.currentUser = currentUser;
    }

    public String getBody() {
        return body_message;
    }

    public String getUserName() {
        return name_user;
    }

    public boolean isCurrentUser() {
        return currentUser;
    }
}