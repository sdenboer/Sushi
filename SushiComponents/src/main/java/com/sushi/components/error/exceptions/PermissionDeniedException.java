package com.sushi.components.error.exceptions;

import com.sushi.components.message.serving.ServingStatus;

import java.util.UUID;

public class PermissionDeniedException extends SushiException {
    public PermissionDeniedException(UUID orderId) {
        super(ServingStatus.PERMISSION_DENIED, orderId);
    }
}
