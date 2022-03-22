import com.sushi.components.protocol.order.push.SushiPushOrder;
import com.sushi.components.protocol.order.push.SushiPushOrderService;
import com.sushi.components.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class FileCopyTest extends AbstractTest {

    private static final int PORT = 9999;

    List<File> files = new ArrayList<>();
    List<File> targetFiles = new ArrayList<>();

    @Before
    @Override
    public void setUp() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get("/tmp/input"))) {
            paths.filter(Files::isRegularFile).map(Path::toFile)
                    .forEach(f -> {
                        try {
                            File tf = new File(TARGET + "/" + f.getName());
                            Files.deleteIfExists(tf.toPath());
                            targetFiles.add(tf);
                            files.add(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }


    @Test
    public void copyTwoLargeFilesConcurrently() throws InterruptedException {
        final CountDownLatch jobsLatch = new CountDownLatch(1);

        files.forEach(f -> runClient(f.getAbsolutePath()));

        jobsLatch.await();

    }

    private void runClient(final String srcPath) {
        new Thread(() -> {
            final File srcFile = new File(srcPath);
            final TestAsyncClient helper = new TestAsyncClient(srcFile.getName(), srcFile.length(), srcPath);
            helper.run();
        }).start();
    }

    private final class TestAsyncClient implements Runnable {

        private final long size;
        private final String fileName;
        private final String srcPath;

        private TestAsyncClient(final String fileName, final long size, String srcPath) {
            this.size = size;
            this.fileName = fileName;
            this.srcPath = srcPath;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();

            SushiPushOrder sushiOrder = SushiPushOrder.newBuilder()
                    .withHost("localhost")
                    .withPort(9999)
                    .withContent("file")
                    .withOrderId(UUID.randomUUID())
                    .withDir("/tmp/output")
                    .withEncryption("AES")
                    .withFileName(this.fileName)
                    .withFileSize(size)
                    .withSrcPath(srcPath)
                    .build();
            SushiPushOrderService sushiPushOrderService = new SushiPushOrderService();
            sushiPushOrderService.send(sushiOrder);

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bestand ").append(this.fileName).append(" van ").append(this.size / (1024 * 1024)).append("MB gekopieerd in ").append(timeElapsed / 1000).append(" seconden");
            System.out.println(stringBuilder);

        }

//        private void confirmAndGo(FileSender sender, final String response) throws IOException {
//            StringUtils.EMPTY
//            if (!response.contains(Constants.CONFIRMATION)) {
//                final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
//                final long bytesRead = sender.read(buffer);
//
//                if (bytesRead > 0) {
//                    confirmAndGo(sender, response + new String(buffer.array()));
//                } else if (bytesRead < 0) {
//                    return;
//                }
//            } else {
//                sender.transfer(srcPath);
//            }
//        }


    }
}
