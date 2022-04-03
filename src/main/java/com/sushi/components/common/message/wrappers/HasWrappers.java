package com.sushi.components.common.message.wrappers;

import com.sushi.components.common.message.MessageMapper;
import lombok.NonNull;

import java.util.Map;

public interface HasWrappers {

    @NonNull
    Map<WrapperField, String> optionalWrappers();

    @NonNull
    Map<WrapperField, String> mandatoryWrappers();

    default String toRequest() {
        return """
                %s
                %s
                           
                """.formatted(
                MessageMapper.serialize(mandatoryWrappers()),
                MessageMapper.serialize(optionalWrappers())
        );
    }
}
