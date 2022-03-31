package com.sushi.components.common.serving;

import com.sushi.components.common.SushiMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

@Getter
public abstract class SushiServing implements SushiMessage<SushiServingWrapper> {

  private final SushiServingStatus sushiServingStatus;
  private final UUID orderId;

  protected SushiServing(SushiServingStatus sushiServingStatus, UUID orderId) {
    this.sushiServingStatus = sushiServingStatus;
    this.orderId = orderId;
  }

  @Override
  public Set<SushiServingWrapper> mandatorySushiWrappers() {
    return Set.of(new SushiServingWrapper(SushiServingWrapperField.STATUS, String.valueOf(sushiServingStatus.getStatusCode())),
        new SushiServingWrapper(SushiServingWrapperField.ORDER_ID, String.valueOf(orderId)));
  }

  public static Map<SushiServingWrapperField, String> mapToHeaders(String request) {
    String[] splitRequest = request.split("\n");

    return Arrays.stream(splitRequest)
        .takeWhile(not(String::isEmpty))
        .map(l -> l.split(": "))
        .collect(toMap(h -> SushiServingWrapperField.fromString(h[0]), h -> h[1]));
  }

  public SushiServingStatus getSushiServingStatus() {
    return sushiServingStatus;
  }

}
