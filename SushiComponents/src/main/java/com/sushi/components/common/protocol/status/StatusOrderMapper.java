package com.sushi.components.common.protocol.status;

import com.sushi.components.common.message.MessageMapper;
import com.sushi.components.common.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.WrapperField.*;

public class StatusOrderMapper implements MessageMapper<StatusOrder> {
    @Override
    public StatusOrder from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return StatusOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .build();
    }
}