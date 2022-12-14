package com.example.tinder.Matches;

// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Title: MatchesObject.java
// Course: CSC 330
// Developer: John Santiago, Peter Wang, Mohamed Bassimbo, Andro Rezkalla
// Date : 12/12/22
// Description: Matches object source code.
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


public class MatchesObject {
    private String userId;
    private String name;
    private String profileImageUrl;
    public MatchesObject (String userId, String name, String profileImageUrl){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}
