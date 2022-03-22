//package com.sushi.components.protocol.methods;
//
//import com.sushi.components.file_transfer.receive.FileReceiver;
//import com.sushi.components.protocol.order.SushiOrder;
//import com.sushi.components.protocol.order.SushiOrderWrapperKey;
//
//import java.net.InetAddress;
//import java.util.Arrays;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class SushiPushMethod extends SushiOrderMethod {
//
//    public void sendRequest(SushiOrder sushiOrder) {
//
//
//    }
//
//    @Override
//    public SushiOrderValidator validate() {
//        return (SushiOrder order) -> {
//            if (order.sushiHost().getPort() <0 && order.sushiHost().getHost() == null) {
//                throw new RuntimeException("HELLO");
//            }
//        };
//    }
//
//    @Override
//    public void sendOrder(SushiOrder... order) {
//        Arrays.stream(order).forEach(
//
//        );
//        int port = order.sushiHost().getPort();
//        InetAddress host = order.sushiHost().getHost();
//        String dir = order.sushiOrderWrappers().get(SushiOrderWrapperKey.DIR);
//        final AtomicBoolean pass = new AtomicBoolean(Boolean.FALSE);
//
//        final FileReceiver fileReceiverAsync = new FileReceiver(host, port, 2, dir, fileWriter -> {
//            jobsLatch.countDown();
//        });
//    }
//
//
//}
