package com.sushi.components.protocol.methods;

import com.sushi.components.protocol.order.SushiOrder;

@FunctionalInterface
public interface SushiOrderValidator {
    void validate(SushiOrder sushiOrder);
}
