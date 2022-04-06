package com.sushi.server.handlers;

import com.sushi.components.message.MessageMapper;
import com.sushi.components.message.order.OrderMethod;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.WrapperField;
import com.sushi.components.utils.Constants;
import com.sushi.server.exceptions.SushiError;
import com.sushi.server.utils.LoggerUtils;
import com.sushi.server.utils.OrderContext;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;
import java.util.UUID;

public class OrderInterceptor {

    private static final Logger logger = Logger.getLogger(OrderInterceptor.class);

    public void intercept(AsynchronousByteChannel socketChannel) {

        ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);

        socketChannel.read(buffer, new StringBuffer(), new CompletionHandler<>() {

            @Override
            public void completed(final Integer result, final StringBuffer attachment) {

                if (result < 0) {
                    logger.info("Empty Buffer");
                } else if (result > 0) {
                    attachment.append(new String(buffer.array()).trim());

                    String message = attachment.toString();

                    Map<WrapperField, String> orderHeaders = MessageMapper.deserialize(message);
                    OrderMethod method = OrderMethod.fromString(orderHeaders.get(WrapperField.METHOD));

                    OrderContext orderContext = new OrderContext(getOrderIdFromOrder(attachment));
                    logger.info(message);

                    new OrderController(socketChannel).handleOrder(method, message, orderContext);

                } else {
                    buffer.clear();
                    socketChannel.read(buffer, attachment, this);
                }
            }

            @Override
            public void failed(final Throwable e, final StringBuffer attachment) {
                OrderContext orderContext = new OrderContext(getOrderIdFromOrder(attachment));
                logger.error(LoggerUtils.createMessage(orderContext), e);
                SushiError.send(socketChannel, ServingStatus.SERVER_ERROR, orderContext);
            }

        });

    }

    private UUID getOrderIdFromOrder(StringBuffer order) {
        String message = order.toString();
        String orderId = MessageMapper.deserialize(message).get(WrapperField.ORDER_ID);
        return UUID.fromString(orderId);
    }
}
