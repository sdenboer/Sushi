package com.sushi.components.common.protocol.remove;

import com.sushi.components.common.message.MessageMapper;
import com.sushi.components.common.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.common.message.wrappers.WrapperField.*;

public class RemoveOrderMapper implements MessageMapper<RemoveOrder> {

    @Override
    public RemoveOrder from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return RemoveOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .build();
    }
}
