package com.example.geoskills2.view.auth;

public class ErrorData {
    private String title;
    private String message;

    public ErrorData(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
