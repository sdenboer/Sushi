package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.serving.SushiServingStatus;

import java.util.UUID;

public class DataLossException extends SushiException {
    public DataLossException(UUID orderId) {
        super(SushiServingStatus.DATA_LOSS, orderId);
    }
}
