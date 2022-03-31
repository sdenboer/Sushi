package com.sushi.components.common.push;

import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapper;
import com.sushi.components.common.serving.SushiServingWrapperField;
import java.util.UUID;
import lombok.Builder;

import java.util.Map;
import java.util.Set;


public class SushiPushServing extends SushiServing {

    private final String content;

    @Builder
    public SushiPushServing(SushiServingStatus sushiServingStatus, UUID orderId, String content) {
        super(sushiServingStatus, orderId);
        this.content = content;
    }

    @Override
    public Set<SushiServingWrapper> optionalSushiWrappers() {
        return Set.of(
                new SushiServingWrapper(SushiServingWrapperField.CONTENT, content)
        );
    }


    public static SushiPushServing fromRequest(String request) {
        Map<SushiServingWrapperField, String> wrappers = SushiServing.mapToHeaders(request);

        return SushiPushServing.builder()
                .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(SushiServingWrapperField.STATUS)))
                .build();
    }


}
