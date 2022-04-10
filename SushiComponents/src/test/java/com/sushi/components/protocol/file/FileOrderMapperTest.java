package com.sushi.components.protocol.file;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileOrderMapperTest {

    private UUID uuid = UUID.fromString("15fa4507-3b20-45db-915f-d2ef7c69e769");

    String message = """
            host: localhost
            port: 8221
            order-id: %s
            dir: /
            file: test.txt
                        
            """.formatted(uuid);

    @Test
    void from_happy() {
        FileOrder fileOrder = new FileOrderMapper().from(message);
        assertEquals(uuid, fileOrder.getOrderId());
        assertEquals("/", fileOrder.getDir());
        assertEquals("test.txt", fileOrder.getFileName());
        assertEquals("localhost", fileOrder.getHost().host());
        assertEquals(8221, fileOrder.getHost().port());
    }

    @Test
    void from_unhappy() {
        FileOrderMapper mapper = new FileOrderMapper();
        assertThrows(RuntimeException.class, () -> mapper.from(""));
    }

}