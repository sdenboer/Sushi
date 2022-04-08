package com.sushi.components.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final long TRANSFER_MAX_SIZE = (1024 * 1024);
    public static final int BUFFER_SIZE = 2048;
    public static final String FILE_DIR = "/home/files";
    public static final int DEFAULT_PORT = 9444;
    public static final int TLS_PORT = 9443;
    public static final String TMP_FILE_SUFFIX = ".partial";

}
