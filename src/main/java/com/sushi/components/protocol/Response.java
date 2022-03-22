package com.sushi.components.protocol;

public class Response {
    private final int responseCode;

    public Response(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String toString() {
        return "Response{" +
            "responseCode=" + responseCode +
            '}';
    }
}
