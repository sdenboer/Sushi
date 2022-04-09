package com.sushi.server.handlers;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class ServerSocketChannelHandler implements Runnable {

    private static final Logger logger = Logger.getLogger(ServerSocketChannelHandler.class);

    protected final InetSocketAddress inetSocketAddress;

    protected ServerSocketChannelHandler(int port) {
        this.inetSocketAddress = new InetSocketAddress(port);
    }

    public abstract void listen() throws IOException;

    @Override
    public void run() {
        try {
            listen();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
