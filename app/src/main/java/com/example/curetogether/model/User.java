package com.example.curetogether.model;

public class User {
    String userId;
    String userName;
    String userAge;
    String userGender;
    String userDisease;
    boolean userRecovered;

    public User() {

    }


    public User(String userName, String userAge, String userGender, String userDisease, boolean userRecovered) {
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userDisease = userDisease;
        this.userRecovered = userRecovered;
    }

    public User(String userId, String userName, String userAge, String userGender, String userDisease, boolean userRecovered) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userDisease = userDisease;
        this.userRecovered = userRecovered;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getUserDisease() {
        return userDisease;
    }
}
