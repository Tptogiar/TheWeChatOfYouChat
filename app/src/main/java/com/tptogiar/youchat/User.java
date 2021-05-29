package com.tptogiar.youchat;

public class User {

    private String userName;
    private int userAvatars;
    private String message;

    public User(String userName, int userAvatars, String message) {
        this.userName = userName;
        this.userAvatars = userAvatars;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserAvatars() {
        return userAvatars;
    }

    public String getMessage() {
        return message;
    }
}
