package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.serving.SushiServingStatus;

import java.util.UUID;

public class ServerErrorException extends SushiException {

    public ServerErrorException(Throwable e, UUID orderId) {
        super(SushiServingStatus.SERVER_ERROR, orderId);
        e.printStackTrace();
    }
}
