import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sushi.components.client.SushiFileOrderService;
import com.sushi.components.client.SushiPullOrderService;
import com.sushi.components.client.SushiRemoveOrderService;
import com.sushi.components.common.message.order.SushiFileOrder;
import com.sushi.components.common.message.order.SushiPullOrder;
import com.sushi.components.common.message.order.SushiRemoveOrder;
import com.sushi.components.common.message.serving.SushiServing;
import com.sushi.components.common.message.order.SushiPushOrder;
import com.sushi.components.client.SushiPushOrderService;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.FilePayload;
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
        try (Stream<Path> paths = Files.walk(Paths.get("/home/pl00cc/tmp/input"))) {
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
        assertEquals(SushiServingStatus.OK.getStatusCode(), helper.testDelete());
//
//        files.forEach(f -> runClient(f.getAbsolutePath()));
//        jobsLatch.await();

    }

    private void runClient(final String srcPath) {
        new Thread(() -> {
            final File srcFile = new File(srcPath);
            final TestAsyncClient helper = new TestAsyncClient(srcFile.getName(), srcFile.length(), srcPath);
//            helper.r();
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


        public int testPush() {
            long start = System.currentTimeMillis();
            long size = 0;
            Path sourcePath = Paths.get("/home/pl00cc/tmp/input", "xaa");
            try {
                size = Files.size(sourcePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(size);
            SushiPushOrder sushiOrder = SushiPushOrder.builder()
                    .host("localhost")
                    .port(9999)
                    .content("file")
                    .orderId(UUID.randomUUID())
                    .dir("/home/pl00cc/tmp/output")
                    .encryption("AES")
                    .fileName("xaa")
                    .fileSize(size)
                    .payload(new FilePayload(sourcePath))
                    .build();
            SushiPushOrderService sushiPushOrderService = new SushiPushOrderService();
            SushiServing send = sushiPushOrderService.send(sushiOrder);

            System.out.println(send.getSushiServingStatus().getStatusCode());
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Bestand " + "test.tar.gz" + " van " + size / (1024 * 1024) + "MB gekopieerd in " + timeElapsed / 1000 + " seconden");
            return send.getSushiServingStatus().getStatusCode();
        }

        public int testPull() {
            long start = System.currentTimeMillis();

            SushiPullOrder sushiOrder = SushiPullOrder.builder()
                    .host("localhost")
                    .port(9999)
                    .orderId(UUID.randomUUID())
                    .dir("/home/pl00cc/tmp/input")
                    .encryption("AES")
                    .fileName("test.tar.gz")
                    .build();
            SushiPullOrderService sushiPullOrderService = new SushiPullOrderService();
            SushiServing send = sushiPullOrderService.send(sushiOrder);

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Bestand " + this.fileName + " van " + this.size / (1024 * 1024) + "MB gekopieerd in " + timeElapsed / 1000 + " seconden");
            return send.getSushiServingStatus().getStatusCode();
        }

        public int testFile() {

            SushiFileOrder sushiOrder = SushiFileOrder.builder()
                    .host("localhost")
                    .port(9999)
                    .orderId(UUID.randomUUID())
                    .dir("/home/pl00cc/tmp/input")
                    .fileName("xaa")
                    .build();
            SushiFileOrderService sushiPullOrderService = new SushiFileOrderService();
            SushiServing send = sushiPullOrderService.send(sushiOrder);

            return 0;
        }

        public int testDelete() {

            SushiRemoveOrder sushiOrder = SushiRemoveOrder.builder()
                    .host("localhost")
                    .port(9999)
                    .orderId(UUID.randomUUID())
                    .dir("/home/pl00cc/tmp/output")
                    .fileName("xaa")
                    .build();
            SushiRemoveOrderService sushiPullOrderService = new SushiRemoveOrderService();
            SushiServing send = sushiPullOrderService.send(sushiOrder);

            return 0;
        }



    }
}
