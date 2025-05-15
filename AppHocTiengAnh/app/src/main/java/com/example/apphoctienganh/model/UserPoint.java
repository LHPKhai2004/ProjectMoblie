package com.example.apphoctienganh.model;

import com.google.gson.annotations.SerializedName;

public class UserPoint {
    @SerializedName("id")
    private String id;

    @SerializedName("point")
    private int point;

    @SerializedName("time")
    private String time;

    @SerializedName("user")
    private User user;

    public UserPoint(int point, String time, User user) {
        this.point = point;
        this.time = time;
        this.user = user;
    }

    public UserPoint() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {
        @SerializedName("account")
        private Account account;

        public User(Account account) {
            this.account = account;
        }

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }
    }

    public static class Account {
        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        public Account(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}