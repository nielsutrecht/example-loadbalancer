package com.nibado.example.loadbalance.rest;

public class MockResponse {
    private int code;
    private String message;
    private long time;

    public MockResponse(int code, String message, long time) {
        this.code = code;
        this.message = message;
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }
}
