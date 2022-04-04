package com.sushi.components.common.error.exceptions;

import com.sushi.components.common.message.serving.ServingStatus;

import java.util.UUID;

public class PermissionDeniedException extends SushiException {
    public PermissionDeniedException(UUID orderId) {
        super(ServingStatus.PERMISSION_DENIED, orderId);
    }
}
