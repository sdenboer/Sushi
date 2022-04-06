package com.sushi.components.message;

import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.WrapperField;
import java.util.Map;

public class OrderFactory implements MessageFactory {

    @Override
    public Message createMessage(Map<WrapperField, String> wrappers,
        PayloadContext payloadContext) {
        return null;
    }

    @Override
    public Message createMessage(Map<WrapperField, String> wrappers) {
        return null;
    }
}
