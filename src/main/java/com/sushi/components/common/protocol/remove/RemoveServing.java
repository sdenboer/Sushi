package com.sushi.components.common.protocol.remove;

import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.message.serving.ServingStatus;
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
