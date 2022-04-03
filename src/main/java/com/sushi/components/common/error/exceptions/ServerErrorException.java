package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.message.serving.ServingStatus;

import java.util.UUID;

public class ServerErrorException extends SushiException {

    public ServerErrorException(Throwable e, UUID orderId) {
        super(ServingStatus.SERVER_ERROR, orderId);
        e.printStackTrace();
    }
}
