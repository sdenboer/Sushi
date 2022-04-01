package com.sushi.components.common.error;

import com.sushi.components.common.serving.SushiServingStatus;

import java.util.UUID;

public class ServerErrorException extends SushiException{

    public ServerErrorException(UUID orderId) {
        super(SushiServingStatus.SERVER_ERROR, orderId);
    }
}
