package com.sushi.components.common;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;

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
        .map(wrapper -> wrapper.getKey() + ": " + wrapper.getValue())
        .collect(Collectors.joining("\n"));
  }

}
