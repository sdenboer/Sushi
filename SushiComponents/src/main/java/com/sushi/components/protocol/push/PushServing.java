package com.sushi.components.protocol.push;

import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.WrapperField;
import lombok.Builder;

import java.util.UUID;

public class PushServing extends Serving {

    private final String content;

    @Builder
    public PushServing(ServingStatus servingStatus, UUID orderId, String content) {
        super(servingStatus, orderId);
        this.content = content;
    }

    @Override
    public void addOptionalWrappers() {
        addWrapper(WrapperField.CONTENT, content);
    }

}
