package com.sushi.server.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sushi.components.utils.Constants.FILE_DIR;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static String removeBaseDir(Path file) {
        return file.toString().replace(FILE_DIR, "");
    }

    public static String filesToPayload(Map<String, String> files) {
        return files.entrySet()
                .stream()
                .map(s -> s.getKey().replace(FILE_DIR, "") + ": " + s.getValue())
                .collect(Collectors.joining("\n"));
    }

    public static String getSHA265HexFromPath(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            return DigestUtils.sha256Hex(is);
        }
    }

}
