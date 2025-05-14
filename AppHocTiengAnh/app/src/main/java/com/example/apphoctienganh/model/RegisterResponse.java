package com.example.apphoctienganh.model;

import java.util.Map;

public class RegisterResponse {
    private boolean result;
    private String message;
    private String code;
    private Map<String, String> data;

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Map<String, String> getData() {
        return data;
    }
}