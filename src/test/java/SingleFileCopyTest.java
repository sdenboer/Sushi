import com.sushi.components.client.SushiFileOrderService;
import com.sushi.components.client.SushiPullOrderService;
import com.sushi.components.client.SushiPushOrderService;
import com.sushi.components.client.SushiRemoveOrderService;
import com.sushi.components.common.message.order.SushiFileOrder;
import com.sushi.components.common.message.order.SushiPullOrder;
import com.sushi.components.common.message.order.SushiPushOrder;
import com.sushi.components.common.message.order.SushiRemoveOrder;
import com.sushi.components.common.message.serving.SushiServing;
import com.sushi.components.common.message.serving.SushiServingStatus;
import com.sushi.components.common.message.wrappers.FilePayload;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class SingleFileCopyTest {

    @Test
    public void push() throws IOException {

        Path sourcePath = Paths.get("/home/pl00cc/tmp/input", "xaa");
        long size = Files.size(sourcePath);

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

        assertEquals(SushiServingStatus.OK, send.getSushiServingStatus());
    }

    @Test
    public void pull() {

        SushiPullOrder sushiOrder = SushiPullOrder.builder()
                .host("localhost")
                .port(9999)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/input")
                .encryption("AES")
                .fileName("xaa")
                .build();
        SushiPullOrderService sushiPullOrderService = new SushiPullOrderService();
        SushiServing send = sushiPullOrderService.send(sushiOrder);

        assertEquals(SushiServingStatus.OK, send.getSushiServingStatus());
    }

    @Test
    public void testFile() {

        SushiFileOrder sushiOrder = SushiFileOrder.builder()
                .host("localhost")
                .port(9999)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/input")
                .fileName("xaa")
                .build();
        SushiFileOrderService sushiPullOrderService = new SushiFileOrderService();
        SushiServing send = sushiPullOrderService.send(sushiOrder);

        assertEquals(SushiServingStatus.OK, send.getSushiServingStatus());
    }

    @Test
    public void remove() {
        SushiRemoveOrder sushiOrder = SushiRemoveOrder.builder()
                .host("localhost")
                .port(9999)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/output")
                .fileName("xaa")
                .build();
        SushiRemoveOrderService sushiPullOrderService = new SushiRemoveOrderService();
        SushiServing send = sushiPullOrderService.send(sushiOrder);

        assertEquals(SushiServingStatus.OK, send.getSushiServingStatus());
    }

    @Test
    public void testChecksum() throws IOException {
        Path path = Paths.get("/home/pl00cc/tmp/output");
        Vector<InputStream> fileInputStreams = new Vector<>();

        try (Stream<Path> stream = Files.list(path)) {
            stream.filter(file -> !Files.isDirectory(file))
                    .forEach(p -> {
                        try {
                            InputStream is = Files.newInputStream(path);
                            fileInputStreams.add(is);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }


        SequenceInputStream seqStream = new SequenceInputStream(fileInputStreams.elements());
        DigestUtils.md5Hex(seqStream);

    }
}
