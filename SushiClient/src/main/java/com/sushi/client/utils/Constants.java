package com.sushi.client.utils;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String FILE = "file";

    public static final String TMP_DIR = "/tmp";

    public static final String LIST_METHOD = "list";
    public static final String FETCH_METHOD = "fetch";
    public static final String BACKUP_METHOD = "backup";
    public static final String VERIFY_METHOD = "verify";
    public static final String REMOVE_METHOD = "remove";

}
