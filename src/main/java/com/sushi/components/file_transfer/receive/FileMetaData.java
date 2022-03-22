package com.sushi.components.file_transfer.receive;

import com.sushi.components.utils.Constants;
import org.apache.commons.lang3.StringUtils;

public record FileMetaData(String fileName, long size) {

    public static FileMetaData from(final String request) {

        final String[] contents = request.replace(Constants.END_MESSAGE_MARKER, StringUtils.EMPTY).split(Constants.MESSAGE_DELIMITTER);
        return new FileMetaData(contents[0], Long.parseLong(contents[1]));
    }

    String getFileName() {
        return this.fileName;
    }

    long getSize() {
        return this.size;
    }
}