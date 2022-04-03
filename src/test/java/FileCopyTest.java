import com.sushi.components.client.file.FileOrderService;
import com.sushi.components.client.pull.PullOrderService;
import com.sushi.components.client.push.PushOrderService;
import com.sushi.components.client.remove.RemoveOrderService;
import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.ContentType;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.protocol.file.FileOrder;
import com.sushi.components.common.protocol.pull.PullOrder;
import com.sushi.components.common.protocol.push.PushOrder;
import com.sushi.components.common.protocol.remove.RemoveOrder;
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

import static org.junit.Assert.assertEquals;

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
        assertEquals(ServingStatus.OK.getStatusCode(), helper.testDelete());
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

    private final class TestAsyncClient {

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
            PushOrder sushiOrder = PushOrder.builder()
                    .host("localhost")
                    .port(9999)
                    .contentType(ContentType.FILE)
                    .orderId(UUID.randomUUID())
                    .dir("/home/pl00cc/tmp/output")
                    .encryption("AES")
                    .fileName("xaa")
                    .fileSize(size)
                    .payload(new FilePayload(sourcePath))
                    .build();
            PushOrderService sushiPushOrderService = new PushOrderService();
            Serving send = sushiPushOrderService.send(sushiOrder);

            System.out.println(send.getServingStatus().getStatusCode());
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Bestand " + "test.tar.gz" + " van " + size / (1024 * 1024) + "MB gekopieerd in " + timeElapsed / 1000 + " seconden");
            return send.getServingStatus().getStatusCode();
        }

        public int testPull() {
            long start = System.currentTimeMillis();

            PullOrder sushiOrder = PullOrder.builder()
                    .host("localhost")
                    .port(9999)
                    .orderId(UUID.randomUUID())
                    .dir("/home/pl00cc/tmp/input")
                    .encryption("AES")
                    .fileName("test.tar.gz")
                    .build();
            PullOrderService sushiPullOrderService = new PullOrderService();
            Serving send = sushiPullOrderService.send(sushiOrder);

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Bestand " + this.fileName + " van " + this.size / (1024 * 1024) + "MB gekopieerd in " + timeElapsed / 1000 + " seconden");
            return send.getServingStatus().getStatusCode();
        }

        public int testFile() {

            FileOrder sushiOrder = FileOrder.builder()
                    .host("localhost")
                    .port(9999)
                    .orderId(UUID.randomUUID())
                    .dir("/home/pl00cc/tmp/input")
                    .fileName("xaa")
                    .build();
            FileOrderService sushiPullOrderService = new FileOrderService();
            Serving send = sushiPullOrderService.send(sushiOrder);

            return 0;
        }

        public int testDelete() {

            RemoveOrder sushiOrder = RemoveOrder.builder()
                    .host("localhost")
                    .port(9999)
                    .orderId(UUID.randomUUID())
                    .dir("/home/pl00cc/tmp/output")
                    .fileName("xaa")
                    .build();
            RemoveOrderService sushiPullOrderService = new RemoveOrderService();
            Serving send = sushiPullOrderService.send(sushiOrder);

            return 0;
        }


    }
}
