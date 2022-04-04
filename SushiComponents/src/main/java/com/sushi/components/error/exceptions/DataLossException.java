package com.sushi.components.error.exceptions;

import com.sushi.components.message.serving.ServingStatus;

import java.util.UUID;

public class DataLossException extends SushiException {
    public DataLossException(UUID orderId) {
        super(ServingStatus.DATA_LOSS, orderId);
    }
}
