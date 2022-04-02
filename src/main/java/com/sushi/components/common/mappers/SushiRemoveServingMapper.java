package com.sushi.components.common.mappers;

import com.sushi.components.common.message.serving.SushiRemoveServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.SushiWrapperField;

import java.util.Map;

import static com.sushi.components.common.message.wrappers.SushiWrapperField.STATUS;

public class SushiRemoveServingMapper implements SushiMessageMapper<SushiRemoveServing> {

    @Override
    public SushiRemoveServing from(String request) {
        Map<SushiWrapperField, String> wrappers = SushiMessageMapper.deserialize(request);

        return SushiRemoveServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
                .build();
    }
}
