package com.sushi.client.cmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.Option;

import static com.sushi.client.utils.Constants.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandLineOptions {

    public static final Option hostOption = Option.builder()
            .option("h")
            .longOpt(HOST)
            .hasArg(true)
            .desc("host ip address")
            .build();

    public static final Option portOption = Option.builder()
            .option("p")
            .longOpt(PORT)
            .hasArg(true)
            .desc("host port")
            .build();

    public static final Option fileOption = Option.builder()
            .option("f")
            .longOpt(FILE)
            .hasArg(true)
            .desc("/path/to/remote/file")
            .build();

    public static final Option localToRemoteFileOption = Option.builder()
            .option("f")
            .longOpt(FILE)
            .hasArg(true)
            .desc("/path/to/local/file:/path/to/remote/file")
            .build();


    //Methods
    public static final Option listMethod = Option.builder()
            .option("l")
            .longOpt(LIST_METHOD)
            .hasArg(false)
            .desc("list all files in (root) directory")
            .build();

    public static final Option fetchMethod = Option.builder()
            .option("f")
            .longOpt(FETCH_METHOD)
            .hasArg(false)
            .desc("fetch a file")
            .build();

    public static final Option backupMethod = Option.builder()
            .option("b")
            .longOpt(BACKUP_METHOD)
            .hasArg(false)
            .desc("push a file")
            .build();


    public static final Option verifyMethod = Option.builder()
            .option("v")
            .longOpt(VERIFY_METHOD)
            .hasArg(false)
            .desc("checksum of all files and directories")
            .build();

    public static final Option removeMethod = Option.builder()
            .option("rm")
            .longOpt(REMOVE_METHOD)
            .hasArg(false)
            .desc("remove a file")
            .build();


}
