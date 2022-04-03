package com.sushi.components.common.message.wrappers;

import com.sushi.components.common.message.MessageMapper;
import lombok.NonNull;

import java.util.Map;

public interface HasWrappers {

    @NonNull
    Map<WrapperField, String> optionalSushiWrappers();

    @NonNull
    Map<WrapperField, String> mandatorySushiWrappers();

    default String toRequest() {
        return """
                %s
                %s
                           
                """.formatted(
                MessageMapper.serialize(mandatorySushiWrappers()),
                MessageMapper.serialize(optionalSushiWrappers())
        );
    }
}
