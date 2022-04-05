package com.sushi.client;

public class Client {

    public static void main(String[] args) {
        args = new String[]{"--list", "-h localhost", "-p 9443", "-f /tmp/output"};

        final String[] finalArgs = args;
        for (int i = 0; i < 100; i++) {
            new Thread(() -> CommandLineHandler.parse(finalArgs)).start();
        }

    }
}
