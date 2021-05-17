package com.example.stopwatch;

public class user {
    String userId;
    String userName;
    String userAge;
    String userGender;
    String userDisease;


    public user(String userId, String userName, String userAge, String userGender, String userDisease) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userDisease = userDisease;
    }
    public String getUserId(){
    return userId;
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
