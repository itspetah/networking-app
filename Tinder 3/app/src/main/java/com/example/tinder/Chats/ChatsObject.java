package com.example.tinder.Chats;

// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Title: ChatsObject.java
// Course: CSC 330
// Developer: John Santiago, Peter Wang, Mohamed Bassimbo, Andro Rezkalla
// Date : 12/12/22
// Description: object main source code.
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class ChatsObject {
    private String message;
    private Boolean currentUser;

    public ChatsObject(String message, Boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }
}
