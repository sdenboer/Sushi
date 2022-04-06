package com.sushi.components.message;

import com.sushi.components.message.wrappers.PayloadContext;
import com.sushi.components.message.wrappers.WrapperField;
import java.util.Map;

public interface MessageFactory {

    Message createMessage(Map<WrapperField, String> wrappers, PayloadContext payloadContext);

    Message createMessage(Map<WrapperField, String> wrappers);

}
