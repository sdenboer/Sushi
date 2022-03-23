package com.sushi.components.common.serving;

import com.sushi.components.common.SushiWrapper;

public class SushiServingWrapper extends SushiWrapper {

    public SushiServingWrapper(SushiServingWrapperField key, String value) {
        super(key.getValue(), value);
    }

}