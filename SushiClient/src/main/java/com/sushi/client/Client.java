package com.sushi.client;

import com.sushi.client.cmd.CommandLineHandler;

public class Client {

    public static void main(String[] args) {
        new CommandLineHandler().parse(args);
    }
}
