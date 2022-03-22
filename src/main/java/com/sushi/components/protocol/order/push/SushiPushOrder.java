package com.sushi.components.protocol.order.push;

import com.sushi.components.protocol.SushiHost;
import com.sushi.components.protocol.order.SushiOrder;
import com.sushi.components.protocol.order.SushiOrderMethod;
import com.sushi.components.protocol.order.SushiOrderWrapper;
import com.sushi.components.protocol.order.SushiOrderWrapperField;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sushi.components.protocol.order.SushiOrderWrapperField.*;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

public class SushiPushOrder extends SushiOrder {


    private static final SushiOrderMethod METHOD = SushiOrderMethod.PUSH;

    private final String dir;
    private final String fileName;
    private final String encryption;
    private final String content;
    private final long fileSize;

    private final String srcPath;

    public SushiPushOrder(String host, int port, UUID orderId, String dir, String fileName, String encryption, String content, long fileSize, String srcPath) {
        super(METHOD, new SushiHost(host, port), orderId);
        this.dir = dir;
        this.fileName = fileName;
        this.encryption = encryption;
        this.content = content;
        this.fileSize = fileSize;
        this.srcPath = srcPath;
    }

    public static SushiPushOrderBuilder newBuilder() {
        return new SushiPushOrderBuilder();
    }

    @Override
    public Set<SushiOrderWrapper> optionalSushiOrderWrappers() {
        return Set.of(
                new SushiOrderWrapper(SushiOrderWrapperField.DIR, dir),
                new SushiOrderWrapper(SushiOrderWrapperField.FILE, fileName),
                new SushiOrderWrapper(SushiOrderWrapperField.ENCRYPTION, encryption),
                new SushiOrderWrapper(SushiOrderWrapperField.CONTENT, content),
                new SushiOrderWrapper(SushiOrderWrapperField.FILE_SIZE, String.valueOf(fileSize))
        );
    }

    public static class SushiPushOrderBuilder {

        private String host;
        private int port;
        private UUID orderId;
        private String dir;
        private String fileName;
        private String encryption;
        private String content;
        private long fileSize;
        private String srcPath;

        private SushiPushOrderBuilder() {
        }

        public SushiPushOrderBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public SushiPushOrderBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public SushiPushOrderBuilder withOrderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public SushiPushOrderBuilder withDir(String dir) {
            this.dir = dir;
            return this;
        }

        public SushiPushOrderBuilder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public SushiPushOrderBuilder withEncryption(String encryption) {
            this.encryption = encryption;
            return this;
        }

        public SushiPushOrderBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public SushiPushOrderBuilder withFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public SushiPushOrderBuilder withSrcPath(String srcPath) {
            this.srcPath = srcPath;
            return this;
        }

        public SushiPushOrder build() {
            return new SushiPushOrder(host, port, orderId, dir, fileName, encryption, content, fileSize, srcPath);
        }

    }

    public String getDir() {
        return dir;
    }

    public String getFileName() {
        return fileName;
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

    public String getSrcPath() {
        return srcPath;
    }

    public static SushiPushOrder fromRequest(String request) {
        String[] splitRequest = request.split("\n");

        Map<SushiOrderWrapperField, String> wrappers = Arrays.stream(splitRequest)
                .takeWhile(not(String::isEmpty))
                .map(l -> l.split(": "))
                .collect(toMap(h -> SushiOrderWrapperField.fromString(h[0]), h -> h[1]));

        return SushiPushOrder.newBuilder()
                .withHost(wrappers.get(HOST))
                .withPort(Integer.parseInt(wrappers.get(PORT)))
                .withOrderId(UUID.fromString(wrappers.get(ORDER_ID)))
                .withDir(wrappers.get(DIR))
                .withFileName(wrappers.get(FILE))
                .withEncryption(wrappers.get(ENCRYPTION))
                .withContent(wrappers.get(CONTENT))
                .withFileSize((Long.parseLong(wrappers.get(FILE_SIZE))))
                .build();
    }
}
