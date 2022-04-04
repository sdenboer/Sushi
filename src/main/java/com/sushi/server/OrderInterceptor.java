package com.sushi.server;

import com.sushi.components.common.OrderContext;
import com.sushi.components.common.error.exceptions.InvalidRequestException;
import com.sushi.components.common.error.exceptions.ServerErrorException;
import com.sushi.components.common.message.MessageMapper;
import com.sushi.components.common.message.order.OrderMethod;
import com.sushi.components.common.message.wrappers.WrapperField;
import com.sushi.components.utils.Constants;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;
import java.util.UUID;

public class OrderInterceptor {

    public void intercept(AsynchronousByteChannel socketChannel) {

        ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);

        socketChannel.read(buffer, new StringBuffer(), new CompletionHandler<>() {

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

                    new OrderController(socketChannel).handleOrder(method, message, orderContext);

                } else {
                    buffer.clear();
                    socketChannel.read(buffer, attachment, this);
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
