package com.sushi.components.common.message.serving;

import java.util.Arrays;

public enum ServingStatus {

    OK(0),
    INVALID(1),
    PERMISSION_DENIED(2),
    ABORTED(3),
    DATA_LOSS(4),
    NOT_IMPLEMENTED(5),
    EXISTS(6),
    SERVER_ERROR(7),
    NOT_FOUND(8);

    private final int statusCode;

    ServingStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public static ServingStatus fromString(String text) {
        return Arrays.stream(ServingStatus.values())
                .filter(value -> value.statusCode == Integer.parseInt(text))
                .findFirst()
                .orElseThrow(RuntimeException::new);

    }

    public int getStatusCode() {
        return statusCode;
    }
}
