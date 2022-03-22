package com.sushi.components.protocol.order;

public class SushiOrderTest {
    private String method = "method";
    private String host = "localhost";
    private String port = "9443";
    private String dir = "/tmp/output/";
    private String file = "test";
    private String encryption = "AES-256";
    private String content = "file";
    private final long fileSize;
    private final String input;

    public SushiOrderTest(long fileSize, String input) {
        this.fileSize = fileSize;
        this.input = input;
    }

    public String getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDir() {
        return dir;
    }

    public String getFile() {
        return file;
    }

    public String getEncryption() {
        return encryption;
    }

    public String getContent() {
        return content;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getInput() {
        return input;
    }
}
