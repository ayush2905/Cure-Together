package com.example.curetogether.model;

public class User {
    private String userId;
    private String userName;
    private String userAge;
    private String userGender;
    private String userDisease;
    private String userRecovered;

    public User() {

    }


    public User(String userName, String userAge, String userGender, String userDisease, String userRecovered) {
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        if (!userDisease.equals(""))
            this.userDisease = userDisease;
        if (!userRecovered.equals(""))
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserDisease() {
        return userDisease;
    }

    public void setUserDisease(String userDisease) {
        this.userDisease = userDisease;
    }

    public String getUserRecovered() {
        return userRecovered;
    }

    public void setUserRecovered(String userRecovered) {
        this.userRecovered = userRecovered;
    }
}
