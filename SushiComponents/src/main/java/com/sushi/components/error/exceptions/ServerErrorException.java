package com.sushi.components.error.exceptions;

import com.sushi.components.message.serving.ServingStatus;

import java.util.UUID;

public class ServerErrorException extends SushiException {

    public ServerErrorException(Throwable e, UUID orderId) {
        super(ServingStatus.SERVER_ERROR, orderId);
        e.printStackTrace();
    }
}
