package com.sushi.components.protocol.pull;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PullOrderMapperTest {

    private UUID uuid = UUID.fromString("15fa4507-3b20-45db-915f-d2ef7c69e769");

    String message = """
            host: localhost
            port: 8221
            order-id: %s
            dir: /
            file: test.txt
                        
            """.formatted(uuid);

    @Test
    void from() {
        PullOrder order = new PullOrderMapper().from(message);
        assertEquals(uuid, order.getOrderId());
        assertEquals("/", order.getDir());
        assertEquals("test.txt", order.getFileName());
        assertEquals("localhost", order.getHost().host());
        assertEquals(8221, order.getHost().port());
    }

    @Test
    void from_unhappy() {
        PullOrderMapper mapper = new PullOrderMapper();
        assertThrows(RuntimeException.class, () -> mapper.from(""));
    }
}