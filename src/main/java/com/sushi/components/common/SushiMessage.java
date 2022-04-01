package com.sushi.components.common;

import lombok.NonNull;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public interface SushiMessage<T extends SushiWrapper> {

    @NonNull
    Set<T> optionalSushiWrappers();

    @NonNull
    Set<T> mandatorySushiWrappers();

    default String toRequest() {
        return """
                %s
                %s
                           
                """.formatted(
                sushiWrappersToRequest(mandatorySushiWrappers()),
                sushiWrappersToRequest(optionalSushiWrappers())
        );
    }

    private String sushiWrappersToRequest(Set<T> sushiServingWrappers) {
        return sushiServingWrappers.stream()
                .filter(wrap -> Objects.nonNull(wrap.getValue()))
                .map(wrapper -> wrapper.getKey() + ": " + wrapper.getValue())
                .collect(Collectors.joining("\n"));
    }

}
