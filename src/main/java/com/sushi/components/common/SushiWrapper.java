package com.sushi.components.common;

public abstract class SushiWrapper {

    private final String key;
    private final String value;

    protected SushiWrapper(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
