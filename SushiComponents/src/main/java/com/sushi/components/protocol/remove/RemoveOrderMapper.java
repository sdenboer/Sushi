package com.sushi.components.protocol.remove;

import static com.sushi.components.message.wrappers.WrapperField.DIR;
import static com.sushi.components.message.wrappers.WrapperField.FILE;
import static com.sushi.components.message.wrappers.WrapperField.HOST;
import static com.sushi.components.message.wrappers.WrapperField.ORDER_ID;
import static com.sushi.components.message.wrappers.WrapperField.PORT;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.wrappers.WrapperField;
import java.util.Map;
import java.util.UUID;

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
