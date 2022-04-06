package com.sushi.components.senders;

import com.sushi.components.utils.OnComplete;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ByteChannel;

public interface Sender {

    void send(ByteChannel socketChannel, String content);

    void send(AsynchronousByteChannel socketChannel, String content, OnComplete onComplete);

}
