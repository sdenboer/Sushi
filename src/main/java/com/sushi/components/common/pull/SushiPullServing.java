package com.sushi.components.common.pull;

import static com.sushi.components.common.serving.SushiServingWrapperField.*;

import com.sushi.components.common.order.SushiOrderWrapper;
import com.sushi.components.common.order.SushiOrderWrapperField;
import com.sushi.components.common.push.SushiPushServing;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapper;
import com.sushi.components.common.serving.SushiServingWrapperField;
import java.util.Collections;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SushiPullServing extends SushiServing {

    private final String encryption;
    private final String content;
    private final long fileSize;
    @Builder
    public SushiPullServing(SushiServingStatus sushiServingStatus, UUID orderId, String encryption, String content, long fileSize) {
        super(sushiServingStatus, orderId);
        this.encryption = encryption;
        this.content = content;
        this.fileSize = fileSize;
    }

    @Override
    public Set<SushiServingWrapper> optionalSushiWrappers() {
        return Set.of(
            new SushiServingWrapper(SushiServingWrapperField.ENCRYPTION, encryption),
            new SushiServingWrapper(SushiServingWrapperField.CONTENT, content),
            new SushiServingWrapper(SushiServingWrapperField.FILE_SIZE, String.valueOf(fileSize))
        );
    }

    public static SushiPullServing fromRequest(String request) {
        Map<SushiServingWrapperField, String> wrappers = SushiServing.mapToHeaders(request);

        return SushiPullServing.builder()
            .sushiServingStatus(SushiServingStatus.fromString(wrappers.get(STATUS)))
            .encryption(wrappers.get(ENCRYPTION))
            .orderId(UUID.fromString(wrappers.get(ORDER_ID)))
            .fileSize(Long.parseLong(wrappers.get(FILE_SIZE)))
            .content(wrappers.get(CONTENT))

            .build();
    }
}
