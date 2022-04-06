package com.sushi.client.cmd;

import static com.sushi.client.utils.Constants.BACKUP_METHOD;
import static com.sushi.client.utils.Constants.FETCH_METHOD;
import static com.sushi.client.utils.Constants.LIST_METHOD;
import static com.sushi.client.utils.Constants.REMOVE_METHOD;
import static com.sushi.client.utils.Constants.VERIFY_METHOD;

import com.sushi.client.utils.Constants;
import org.apache.commons.cli.Option;

public class CommandLineOptions {

    public static final Option hostOption = Option.builder()
        .option("h")
        .longOpt(Constants.HOST)
        .hasArg(true)
        .desc("host ip address")
        .build();

    public static final Option portOption = Option.builder()
        .option("p")
        .longOpt(Constants.PORT)
        .hasArg(true)
        .desc("host port")
        .build();

    public static final Option fileOption = Option.builder()
        .option("f")
        .longOpt(Constants.FILE)
        .hasArg(true)
        .desc("/path/to/remote/file")
        .build();

    public static final Option localToRemoteFileOption = Option.builder()
        .option("f")
        .longOpt(Constants.FILE)
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
