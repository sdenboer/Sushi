package com.sushi.components.protocol.push;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PushOrderMapperTest {

    private UUID uuid = UUID.fromString("15fa4507-3b20-45db-915f-d2ef7c69e769");

    String message = """
            host: localhost
            port: 8221
            order-id: %s
            dir: /
            file: test.txt
            content-length: 21112
                        
            """.formatted(uuid);

    @Test
    void from_happy() {
        PushOrder order = new PushOrderMapper().from(message);
        assertEquals(uuid, order.getOrderId());
        assertEquals("/", order.getDir());
        assertEquals("test.txt", order.getFileName());
        assertEquals("localhost", order.getHost().host());
        assertEquals(8221, order.getHost().port());
    }

    @Test
    void from_unhappy() {
        PushOrderMapper mapper = new PushOrderMapper();
        assertThrows(RuntimeException.class, () -> mapper.from(""));
    }
}