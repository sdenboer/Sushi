import com.sushi.client.OrderController;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import com.sushi.components.message.wrappers.ContentType;
import com.sushi.components.message.wrappers.FilePayload;
import com.sushi.components.protocol.file.FileOrder;
import com.sushi.components.protocol.pull.PullOrder;
import com.sushi.components.protocol.push.PushOrder;
import com.sushi.components.protocol.remove.RemoveOrder;
import com.sushi.components.protocol.status.StatusOrder;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.sushi.components.utils.Constants.DEFAULT_PORT;
import static com.sushi.components.utils.Constants.TLS_PORT;
import static org.junit.Assert.assertEquals;

public class SingleFileCopyTest {

    @Test
    public void push() throws IOException {

        Path sourcePath = Paths.get("/home/pl00cc/tmp/input", "xaa");
        long size = Files.size(sourcePath);

        PushOrder sushiOrder = PushOrder.builder()
                .host("localhost")
                .port(TLS_PORT)
                .contentType(ContentType.FILE)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/output")
                .encryption("AES")
                .fileName("xaa")
                .fileSize(size)
                .payload(new FilePayload(sourcePath))
                .build();
        Serving serving = new OrderController().handleOrder(sushiOrder);

        assertEquals(ServingStatus.OK, serving.getServingStatus());
    }

    @Test
    public void pull() {

        PullOrder sushiOrder = PullOrder.builder()
                .host("localhost")
                .port(DEFAULT_PORT)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/input")
                .encryption("AES")
                .fileName("xaa")
                .build();
        Serving serving = new OrderController().handleOrder(sushiOrder);

        assertEquals(ServingStatus.OK, serving.getServingStatus());
    }

    @Test
    public void testFile() {

        FileOrder sushiOrder = FileOrder.builder()
                .host("localhost")
                .port(TLS_PORT)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/output")
                .fileName("test.txt")
                .build();
        Serving serving = new OrderController().handleOrder(sushiOrder);

        assertEquals(ServingStatus.OK, serving.getServingStatus());
    }

    @Test
    public void remove() {
        RemoveOrder sushiOrder = RemoveOrder.builder()
                .host("localhost")
                .port(DEFAULT_PORT)
                .orderId(UUID.randomUUID())
                .dir("/home/pl00cc/tmp/output")
                .fileName("xaa")
                .build();
        Serving serving = new OrderController().handleOrder(sushiOrder);

        assertEquals(ServingStatus.OK, serving.getServingStatus());
    }

    @Test
    public void testChecksum() {
        StatusOrder sushiOrder = StatusOrder.builder()
                .host("localhost")
                .port(DEFAULT_PORT)
                .orderId(UUID.randomUUID())
                .build();
        Serving serving = new OrderController().handleOrder(sushiOrder);

        assertEquals(ServingStatus.OK, serving.getServingStatus());

    }

}