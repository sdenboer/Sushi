package com.sushi.components.common.order;

public interface SushiOrderMapper<T extends SushiOrder> {

    T from(String request);
}
