package com.sushi.components.protocol.remove;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RemoveOrderMapperTest {

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
        RemoveOrder order = new RemoveOrderMapper().from(message);
        assertEquals(uuid, order.getOrderId());
        assertEquals("/", order.getDir());
        assertEquals("test.txt", order.getFileName());
        assertEquals("localhost", order.getHost().host());
        assertEquals(8221, order.getHost().port());
    }

    @Test
    void from_unhappy() {
        RemoveOrderMapper mapper = new RemoveOrderMapper();
        assertThrows(RuntimeException.class, () -> mapper.from(""));
    }

}