package com.sushi.server;

import com.sushi.server.handlers.AsyncServerSocketChannelHandler;
import com.sushi.server.handlers.AsyncTlsServerSocketChannelHandler;

import java.util.concurrent.ForkJoinPool;

import static com.sushi.components.utils.Constants.DEFAULT_PORT;
import static com.sushi.components.utils.Constants.TLS_PORT;

public class Server {

    public static void main(String[] args) {
        acceptConnections();
    }

    public static void acceptConnections() {
        final ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(new AsyncTlsServerSocketChannelHandler(TLS_PORT));
        pool.execute(new AsyncServerSocketChannelHandler(DEFAULT_PORT));
    }

}
