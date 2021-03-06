package com.sushi.components.protocol.file;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.wrappers.WrapperField;

import java.util.Map;
import java.util.UUID;

import static com.sushi.components.message.wrappers.WrapperField.*;

public class FileOrderMapper implements MessageMapper<FileOrder> {

    @Override
    public FileOrder from(String request) {
        Map<WrapperField, String> wrappers = MessageMapper.deserialize(request);

        return FileOrder.builder()
                .host(wrappers.get(HOST))
                .port(Integer.parseInt(wrappers.get(PORT)))
                .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .dir(wrappers.get(DIR))
                .fileName(wrappers.get(FILE))
                .build();
    }
}
