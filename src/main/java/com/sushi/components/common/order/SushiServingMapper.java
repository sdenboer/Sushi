package com.sushi.components.common.order;

import com.sushi.components.common.serving.SushiServing;

public interface SushiServingMapper<T extends SushiServing> {

    T from(String request);
}
