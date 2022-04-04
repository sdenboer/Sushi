package com.sushi.components.protocol.remove;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import lombok.Builder;

import java.util.UUID;

public class RemoveServing extends Serving {

    @Builder
    public RemoveServing(ServingStatus servingStatus, UUID orderId) {
        super(servingStatus, orderId);
    }

    @Override
    public void addOptionalWrappers() {
        //not needed
    }
}
