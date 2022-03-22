package com.sushi.components.connection;

import com.sushi.components.utils.Constants;

import java.net.InetAddress;

public class SushiRoute {

    private final SushiHost targetHost;
    private final InetAddress local;
    private final boolean secured;


    public SushiRoute(SushiHost targetHost, InetAddress local, boolean secured) {
        this.targetHost = normalize(targetHost);
        this.local = local;
        this.secured = secured;
    }

    private static int getDefaultPort() {
        return Constants.DEFAULT_PORT;
    }

    private static SushiHost normalize(SushiHost targetHost) {
        if (targetHost.getPort() >= 0) {
            return targetHost;
        }
        return new SushiHost(targetHost.getHost(), getDefaultPort());

    }
}
