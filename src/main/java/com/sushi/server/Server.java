package com.sushi.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static com.sushi.components.utils.Constants.DEFAULT_PORT;
import static com.sushi.components.utils.Constants.TLS_PORT;

public class Server {

    private final List<ServerSocketChannelHandler> serverSocketChannelHandlers = new ArrayList<>();

    public Server() {
        serverSocketChannelHandlers.add(new AsyncTlsServerSocketChannelHandler(TLS_PORT));
        serverSocketChannelHandlers.add(new AsyncServerSocketChannelHandler(DEFAULT_PORT));
    }

    public static void main(String[] args) {
        acceptConnections(new Server());
    }

    public static void acceptConnections(Server server) {
        final ForkJoinPool pool = ForkJoinPool.commonPool();
        server.serverSocketChannelHandlers.forEach(pool::execute);
    }

}
