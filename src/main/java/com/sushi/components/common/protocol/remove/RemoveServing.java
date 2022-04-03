package com.sushi.components.common.protocol.remove;

import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.WrapperField;
import lombok.Builder;
import lombok.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class RemoveServing extends Serving {

    @Builder
    public RemoveServing(ServingStatus servingStatus, UUID orderId) {
        super(servingStatus, orderId);
    }

    @Override
    public @NonNull Map<WrapperField, String> optionalWrappers() {
        return Collections.emptyMap();
    }
}
