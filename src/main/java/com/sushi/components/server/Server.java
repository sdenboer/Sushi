package com.sushi.components.server;

import java.io.IOException;

public class Server {

    private final AsynchronousServerSocketChannelHandler handler;

    public Server() {
        this.handler = new AsynchronousServerSocketChannelHandler();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        while (true) {
            server.acceptConnections();
            System.in.read();
        }
    }

    public void acceptConnections() {
        handler.listen();
    }

}
