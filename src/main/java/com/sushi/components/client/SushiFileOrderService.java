package com.sushi.components.client;

public abstract class SushiFileOrderService {

    protected final String srcPath;

    protected SushiFileOrderService(String srcPath) {
        this.srcPath = srcPath;
    }
}
