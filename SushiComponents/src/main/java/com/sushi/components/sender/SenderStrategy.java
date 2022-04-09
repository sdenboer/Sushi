package com.sushi.components.sender;

import java.nio.channels.Channel;

public interface SenderStrategy<T extends Channel, V> {

    void send(T socketChannel, V content);

}
