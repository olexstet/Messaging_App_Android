package com.example.messaging_app;

public class Message {
    private String body_message; // message body
    private String name_user; // data of the user that sent this message
    private boolean currentUser; // is this message sent by us?

    public Message(String body, String username, boolean currentUser) {
        this.name_user = username; // pass sender name of the message
        this.body_message = body; // pass body of the message
        this.currentUser = currentUser; // is the message sent by us or by another user.
    }

    // Return content of variables of the Message
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