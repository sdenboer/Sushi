package com.sushi.components.connection;

import java.net.InetAddress;

public class SushiHost {
    private final InetAddress host;
    private final int port;

    public SushiHost(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public InetAddress getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
