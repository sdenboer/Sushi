package com.sushi.components.common;

import com.sushi.components.common.message.SushiMessage;

@FunctionalInterface
public interface OnComplete {

    void onComplete(SushiMessage sushiMessage);
}
