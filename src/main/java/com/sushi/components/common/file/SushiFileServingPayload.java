package com.sushi.components.common.file;

import com.sushi.components.common.serving.SushiServingPayload;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class SushiFileServingPayload implements SushiServingPayload {

    private final Map<String, String> files = new HashMap<>();

    public void addFile(String file, String hash) {
        this.files.put(file, hash);
    }

    public String toRequest() {
        return files.entrySet()
                .stream()
                .map(s -> s.getKey() + ": " + s.getValue())
                .collect(Collectors.joining("\n"));
    }


}
