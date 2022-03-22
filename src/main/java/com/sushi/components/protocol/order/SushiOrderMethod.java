package com.sushi.components.protocol.order;

public enum SushiOrderMethod {

    STATUS("STATUS"),
    FILE("FILE"),
    PUSH("PUSH"),
    PULL("PULL"),
    REMOVE("REMOVE");

    private final String value;

    SushiOrderMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
