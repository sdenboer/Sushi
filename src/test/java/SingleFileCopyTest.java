import com.sushi.components.client.FileOrderService;
import com.sushi.components.client.PullOrderService;
import com.sushi.components.client.PushOrderService;
import com.sushi.components.client.RemoveOrderService;
import com.sushi.components.common.message.serving.Serving;
import com.sushi.components.common.message.serving.ServingStatus;
import com.sushi.components.common.message.wrappers.FilePayload;
import com.sushi.components.common.protocol.file.FileOrder;
import com.sushi.components.common.protocol.pull.PullOrder;
import com.sushi.components.common.protocol.push.PushOrder;
import com.sushi.components.common.protocol.remove.RemoveOrder;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class SingleFileCopyTest {

    @Test
    public void push() throws IOException {

        Path sourcePath = Paths.get("/home/pl00cc/tmp/input", "xaa");
        long size = Files.size(sourcePath);

        PushOrder sushiOrder = PushOrder.builder()
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
        PushOrderService sushiPushOrderService = new PushOrderService();
        Serving send = sushiPushOrderService.send(sushiOrder);

        assertEquals(ServingStatus.OK, send.getSushiServingStatus());
    }

    @Test
    public void pull() {

        PullOrder sushiOrder = PullOrder.builder()
                .host("localhost")
                .port(9999)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/input")
                .encryption("AES")
                .fileName("xaa")
                .build();
        PullOrderService sushiPullOrderService = new PullOrderService();
        Serving send = sushiPullOrderService.send(sushiOrder);

        assertEquals(ServingStatus.OK, send.getSushiServingStatus());
    }

    @Test
    public void testFile() {

        FileOrder sushiOrder = FileOrder.builder()
                .host("localhost")
                .port(9999)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/output")
                .fileName("test.txt")
                .build();
        FileOrderService sushiPullOrderService = new FileOrderService();
        Serving send = sushiPullOrderService.send(sushiOrder);

        assertEquals(ServingStatus.OK, send.getSushiServingStatus());
    }

    @Test
    public void remove() {
        RemoveOrder sushiOrder = RemoveOrder.builder()
                .host("localhost")
                .port(9999)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/output")
                .fileName("xaa")
                .build();
        RemoveOrderService sushiPullOrderService = new RemoveOrderService();
        Serving send = sushiPullOrderService.send(sushiOrder);

        assertEquals(ServingStatus.OK, send.getSushiServingStatus());
    }

    @Test
    public void testChecksum() throws IOException {
        Path path = Paths.get("/home/pl00cc/tmp/output");

        Vector<InputStream> inputStreams = new Vector<>();
        collectInputStreams(path, inputStreams);
        try (SequenceInputStream stream = new SequenceInputStream(inputStreams.elements())) {
            String sha256Hex = DigestUtils.sha256Hex(stream);
            System.out.println(sha256Hex);
        }
    }

    private void collectInputStreams(Path dir,
                                     List<InputStream> foundStreams) {

        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(path -> {
                boolean directory = Files.isDirectory(path);
                if (directory) {
                    collectInputStreams(path, foundStreams);
                } else {
                    try {
                        foundStreams.add(Files.newInputStream(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
