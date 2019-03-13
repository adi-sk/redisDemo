package com.zycus.redis.model;

public class User {
    private String userId ;
    private String name;
    private long followers;
    private String emailId;

    public User(){

    }

    public User(String userId, String name, long followers, String emailId) {
        this.userId = userId;
        this.name = name;
        this.followers = followers;
        this.emailId = emailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
