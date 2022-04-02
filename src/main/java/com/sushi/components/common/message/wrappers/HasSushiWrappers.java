package com.sushi.components.common.message.wrappers;

import com.sushi.components.common.mappers.SushiMessageMapper;
import lombok.NonNull;

import java.util.Map;

public interface HasSushiWrappers {

    @NonNull
    Map<SushiWrapperField, String> optionalSushiWrappers();

    @NonNull
    Map<SushiWrapperField, String> mandatorySushiWrappers();

    default String toRequest() {
        return """
                %s
                %s
                           
                """.formatted(
                SushiMessageMapper.serialize(mandatorySushiWrappers()),
                SushiMessageMapper.serialize(optionalSushiWrappers())
        );
    }
}
