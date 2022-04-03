package com.sushi.components.server;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.InvalidRequestException;
import com.sushi.components.common.error.exceptions.NotImplementedException;
import com.sushi.components.common.error.exceptions.ServerErrorException;
import com.sushi.components.common.message.MessageMapper;
import com.sushi.components.common.message.order.OrderMethod;
import com.sushi.components.common.message.wrappers.WrapperField;
import com.sushi.components.common.protocol.file.FileOrderMapper;
import com.sushi.components.common.protocol.pull.PullOrderMapper;
import com.sushi.components.common.protocol.push.PushOrderMapper;
import com.sushi.components.common.protocol.remove.RemoveOrderMapper;
import com.sushi.components.server.file.FileOrderService;
import com.sushi.components.server.pull.PullOrderService;
import com.sushi.components.server.push.PushOrderService;
import com.sushi.components.server.remove.RemoveOrderService;
import com.sushi.components.utils.Constants;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;
import java.util.UUID;

public class OrderController {

    private final AsynchronousSocketChannel channel;

    public OrderController(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public void handleOrder() {

        ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);

        channel.read(buffer, new StringBuffer(), new CompletionHandler<>() {

            @Override
            public void completed(final Integer result, final StringBuffer attachment) {
                if (result < 0) {
                    throw new InvalidRequestException(getOrderIdFromOrder(attachment));
                } else if (result > 0) {
                    attachment.append(new String(buffer.array()).trim());

                    String message = attachment.toString();


                    Map<WrapperField, String> orderHeaders = MessageMapper.deserialize(message);
                    OrderMethod method = OrderMethod.fromString(orderHeaders.get(WrapperField.METHOD));

                    OrderContext orderContext = new OrderContext(getOrderIdFromOrder(attachment));

                    switch (method) {
                        case PUSH -> new PushOrderService().handle(channel, new PushOrderMapper().from(message), orderContext);
                        case PULL -> new PullOrderService().handle(channel, new PullOrderMapper().from(message), orderContext);
                        case FILE -> new FileOrderService().handle(channel, new FileOrderMapper().from(message), orderContext);
                        case REMOVE -> new RemoveOrderService().handle(channel, new RemoveOrderMapper().from(message), orderContext);
                        default -> throw new NotImplementedException(orderContext.getOrderId());
                    }
                } else {
                    buffer.clear();
                    channel.read(buffer, attachment, this);
                }
            }


            @Override
            public void failed(final Throwable exc, final StringBuffer attachment) {
                throw new ServerErrorException(exc, getOrderIdFromOrder(attachment));
            }

        });
    }

    private UUID getOrderIdFromOrder(StringBuffer order) {
        String message = order.toString();
        String orderId = MessageMapper.deserialize(message).get(WrapperField.ORDER_ID);
        return UUID.fromString(orderId);
    }

}
