package com.sushi.server;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class ServerSocketChannelHandler implements Runnable {

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
            e.printStackTrace();
        }
    }
}
