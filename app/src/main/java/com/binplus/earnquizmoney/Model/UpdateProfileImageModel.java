package com.binplus.earnquizmoney.Model;

public class UpdateProfileImageModel {
    public boolean response;
    public String message;

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public  String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
