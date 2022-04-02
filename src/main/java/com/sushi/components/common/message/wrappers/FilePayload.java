package com.sushi.components.common.message.wrappers;

import java.nio.file.Path;

public record FilePayload(Path path) implements Payload {

    public Path getPath() {
        return path;
    }

}
