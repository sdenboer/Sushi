package com.sushi.components.error;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import lombok.Builder;

import java.util.UUID;

public class ErrorServing extends Serving {

    @Builder
    public ErrorServing(ServingStatus servingStatus, UUID orderId) {
        super(servingStatus, orderId);
    }

    public void addOptionalWrappers() {
        //not needed
    }

}
