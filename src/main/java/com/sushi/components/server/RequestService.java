package com.sushi.components.server;

import static com.sushi.components.protocol.order.SushiOrderMethod.PUSH;

import com.sushi.components.file_transfer.receive.FileWriter;
import com.sushi.components.protocol.order.push.SushiPushOrder;
import com.sushi.components.utils.Constants;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class RequestService {

  public void handleRequest(final AsynchronousSocketChannel channel) {

    final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
    channel.read(buffer, new StringBuffer(), new CompletionHandler<>() {
      @Override
      public void completed(final Integer result, final StringBuffer attachment) {
        if (result < 0) {
          close(channel, null);
          return;
        }

        attachment.append(new String(buffer.array()).trim());

        if (isPushMethod(attachment)) {
          SushiPushOrder pushOrder = SushiPushOrder.fromRequest(attachment.toString());
          new PushRequestService(pushOrder).handle(channel);
        } else {
          buffer.clear();
          channel.read(buffer, attachment, this);
        }
      }


      @Override
      public void failed(final Throwable exc, final StringBuffer attachment) {
        close(channel, null);
        throw new RuntimeException("unable to read meta data", exc);
      }


      private boolean isPushMethod(StringBuffer buffer) {
        return buffer.toString().contains(PUSH.getValue());
      }
    });
  }


  private void close(final AsynchronousSocketChannel channel, final FileWriter proxy) {
    try {
      if (proxy != null) {
        proxy.close();
      }
      channel.close();
    } catch (IOException e) {
      throw new RuntimeException("unable to close channel and FileWriter", e);
    }
  }
}
