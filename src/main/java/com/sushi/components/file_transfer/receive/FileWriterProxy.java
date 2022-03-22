//package com.sushi.components.file_transfer.receive;
//
//import java.io.IOException;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class FileWriterProxy {
//    private final FileWriter fileWriter;
//    private final AtomicLong position;
//    private final long fileSize;
//
//    public FileWriterProxy(String path, String fileName, long fileSize) throws IOException {
//        this.fileWriter = new FileWriter(path + "/" + fileName);
//        this.position = new AtomicLong(0L);
//        this.fileSize = fileSize;
//    }
//
//    public FileWriter getFileWriter() {
//        return fileWriter;
//    }
//
//    public AtomicLong getPosition() {
//        return position;
//    }
//
//    public boolean done() {
//        return this.position.get() == this.fileSize;
//    }
//}
