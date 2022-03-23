package com.sushi.components.server;

import com.sushi.components.common.order.SushiOrder;
import com.sushi.components.common.order.SushiOrderMethod;
import com.sushi.components.common.order.SushiOrderWrapperField;
import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.push.SushiPushOrder;
import com.sushi.components.server.pull.PullOrderService;
import com.sushi.components.server.push.PushOrderService;
import com.sushi.components.utils.ChannelUtils;
import com.sushi.components.utils.CompletionHandlerUtils;
import com.sushi.components.utils.Constants;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class OrderController {

    private final AsynchronousSocketChannel channel;

    public OrderController(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public void handleOrder() {

        final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        channel.read(buffer, new StringBuffer(), new CompletionHandler<>() {
            @Override
            public void completed(final Integer result, final StringBuffer attachment) {
                if (CompletionHandlerUtils.errorOccurred(result)) {
                    ChannelUtils.close(channel);
                    return;
                }

                attachment.append(new String(buffer.array()).trim());

                String message = attachment.toString();

                SushiOrderMethod method = SushiOrderMethod.fromString(
                        SushiOrder.mapToHeaders(message).get(SushiOrderWrapperField.METHOD));

                switch (method) {
                    case PUSH -> new PushOrderService(channel).handle(SushiPushOrder.fromRequest(message));
                    case PULL -> new PullOrderService(channel).handle(SushiPullOrder.fromRequest(message));
                    default -> CompletionHandlerUtils.resume(buffer, channel, attachment, this);
                }
            }


            @Override
            public void failed(final Throwable exc, final StringBuffer attachment) {
                ChannelUtils.close(channel);
                throw new RuntimeException("unable to read meta data", exc);
            }

        });
    }

}
