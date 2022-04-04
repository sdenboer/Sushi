package com.sushi.components.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class OrderContext {

    private final UUID orderId;

}
