package com.sushi.components.message.serving;

import com.sushi.components.message.Message;
import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.WrapperField;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Serving implements Message {

    private final ServingStatus servingStatus;
    private final UUID orderId;
    private final PayloadContext payloadContext;

    public Serving(ServingStatus servingStatus, UUID orderId, PayloadContext payloadContext) {
        this.orderId = orderId;
        this.servingStatus = servingStatus;
        this.payloadContext = payloadContext;
    }

    public Map<WrapperField, String> getWrappers() {
        Map<WrapperField, String> wrappers = new EnumMap<>(WrapperField.class);
        wrappers.put(WrapperField.STATUS, String.valueOf(servingStatus.getStatusCode()));
        wrappers.put(WrapperField.ORDER_ID, String.valueOf(orderId));
        if (hasPayload()) {
            wrappers.put(WrapperField.CONTENT,
                payloadContext.payloadMetaData().contentType().getType());
            wrappers.put(WrapperField.CONTENT_LENGTH,
                String.valueOf(payloadContext.payloadMetaData().contentLength()));
        }
        return wrappers;
    }

}
