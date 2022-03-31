import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sushi.components.client.SushiPullOrderService;
import com.sushi.components.common.pull.SushiPullOrder;
import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.push.SushiPushOrder;
import com.sushi.components.client.SushiPushOrderService;
import com.sushi.components.common.serving.SushiServingStatus;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
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
//                            Files.deleteIfExists(tf.toPath());
                            targetFiles.add(tf);
                            files.add(f);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }


    @Test
    public void copyTwoLargeFilesConcurrently() throws InterruptedException {
        final CountDownLatch jobsLatch = new CountDownLatch(1);

        final File srcFile = new File(files.get(0).getAbsolutePath());
        final TestAsyncClient helper = new TestAsyncClient(srcFile.getName(), srcFile.length(), files.get(0).getAbsolutePath());
        assertEquals(SushiServingStatus.OK.getStatusCode(), helper.run2());
//
//        files.forEach(f -> runClient(f.getAbsolutePath()));
//        jobsLatch.await();

    }

    private void runClient(final String srcPath) {
        new Thread(() -> {
            final File srcFile = new File(srcPath);
            final TestAsyncClient helper = new TestAsyncClient(srcFile.getName(), srcFile.length(), srcPath);
            helper.run2();
        }).start();
    }

    private final class TestAsyncClient  {

        private final long size;
        private final String fileName;
        private final String srcPath;

        private TestAsyncClient(final String fileName, final long size, String srcPath) {
            this.size = size;
            this.fileName = fileName;
            this.srcPath = srcPath;
        }


        public int run() {
            long start = System.currentTimeMillis();

            SushiPushOrder sushiOrder = SushiPushOrder.newBuilder()
                    .host("localhost")
                    .port(9999)
                    .content("file")
                    .orderId(UUID.randomUUID())
                    .dir("/tmp/output")
                    .encryption("AES")
                    .fileName(this.fileName)
                    .fileSize(size)
                    .build();
            SushiPushOrderService sushiPushOrderService = new SushiPushOrderService(srcPath);
            SushiServing send = sushiPushOrderService.send(sushiOrder);

            System.out.println(send.getSushiServingStatus().getStatusCode());
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Bestand " + this.fileName + " van " + this.size / (1024 * 1024) + "MB gekopieerd in " + timeElapsed / 1000 + " seconden");
            return send.getSushiServingStatus().getStatusCode();
        }

        public int run2() {
            long start = System.currentTimeMillis();

            SushiPullOrder sushiOrder = SushiPullOrder.builder()
                    .host("localhost")
                    .port(9999)
                    .orderId(UUID.randomUUID())
                    .dir("/tmp/output")
                    .encryption("AES")
                    .fileName(this.fileName)
                    .build();
            SushiPullOrderService sushiPullOrderService = new SushiPullOrderService(srcPath);
            SushiServing send = sushiPullOrderService.send(sushiOrder);

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Bestand " + this.fileName + " van " + this.size / (1024 * 1024) + "MB gekopieerd in " + timeElapsed / 1000 + " seconden");
//            return send.getSushiServingStatus().getStatusCode();
            return 0;
        }



    }
}
