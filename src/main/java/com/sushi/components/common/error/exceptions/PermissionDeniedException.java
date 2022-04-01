package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.serving.SushiServingStatus;

import java.util.UUID;

public class PermissionDeniedException extends SushiException {
    public PermissionDeniedException(UUID orderId) {
        super(SushiServingStatus.PERMISSION_DENIED, orderId);
    }
}
