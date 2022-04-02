package com.sushi.components.common.pull;

import com.sushi.components.common.serving.SushiServingPayload;

import java.nio.file.Path;

public class SushiPullServingPayload implements SushiServingPayload {

    private final Path path;

    public SushiPullServingPayload(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
