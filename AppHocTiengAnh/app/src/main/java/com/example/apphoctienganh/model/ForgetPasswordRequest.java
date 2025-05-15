package com.example.apphoctienganh.model;

public class ForgetPasswordRequest {
    private String email;

    public ForgetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}