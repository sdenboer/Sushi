package com.sushi.components.common.file;

import com.sushi.components.common.serving.SushiServingPayload;
import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class SushiFileServingPayload implements SushiServingPayload {

    private final Set<Map<String, byte[]>> files = new HashSet<>();

    public void addFile(String file, byte[] hash) {
        this.files.add(Map.of(file, hash));
    }


}
