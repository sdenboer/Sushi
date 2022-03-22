package com.sushi.components.protocol.serving;

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

    private final int status;

    ServingStatus(int status) {
        this.status = status;
    }
}
