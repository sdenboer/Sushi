package com.sushi.components.common.order;

import com.sushi.components.common.SushiWrapper;

public class SushiOrderWrapper extends SushiWrapper {

    public SushiOrderWrapper(SushiOrderWrapperField key, String value) {
        super(key.getValue(), value);
    }

}
