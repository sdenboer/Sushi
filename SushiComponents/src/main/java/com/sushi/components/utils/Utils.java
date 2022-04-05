package com.sushi.components.utils;

import java.nio.file.Path;

public class Utils {

    public static Path getCurrentWorkingDirectory() {
        final String currentDir = System.getProperty("user.dir");
        return Path.of(currentDir);
    }
}
