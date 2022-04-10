package com.sushi.client.cmd;

import com.sushi.client.order.OrderController;
import com.sushi.components.message.order.Order;
import com.sushi.components.message.order.OrderMethod;
import com.sushi.components.message.serving.Serving;
import com.sushi.components.message.serving.ServingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

//Also tests OrderOptionFactory.class
@ExtendWith(MockitoExtension.class)
class CommandLineHandlerTest {

    @Mock
    OrderController orderController;

    @Mock
    Serving serving;

    CommandLineHandler commandLineHandler;

    @Captor
    ArgumentCaptor<Order> orderArgumentCaptor;

    String host = "localhost";
    String port = "9443";

    @BeforeEach
    void setUp() {
        when(serving.getServingStatus()).thenReturn(ServingStatus.OK);
        when(orderController.handleOrder(orderArgumentCaptor.capture())).thenReturn(serving);
        commandLineHandler = new CommandLineHandler(orderController);
    }

    @ParameterizedTest
    @ValueSource(strings = {"--backup", "-b"})
    void parseBackup(String command) {
        String resourcePath = "src/test/resources/home/files/test.txt";
        String[] args = new String[]{command, "-h " + host, "-p " + port, "-f " + resourcePath + ":/tmp/output"};
        commandLineHandler.parse(args);
        assertEquals(OrderMethod.PUSH, orderArgumentCaptor.getValue().getOrderMethod());
    }

    @ParameterizedTest
    @ValueSource(strings = {"--fetch", "-f"})
    void parseFetch(String command) {
        String[] args = new String[]{command, "-h " + host, "-p " + port, "-f /tmp"};
        commandLineHandler.parse(args);
        assertEquals(OrderMethod.PULL, orderArgumentCaptor.getValue().getOrderMethod());
    }

    @ParameterizedTest
    @ValueSource(strings = {"--list", "-l"})
    void parseList(String command) {
        String[] args = new String[]{command, "-h " + host, "-p " + port, "-f /tmp"};
        commandLineHandler.parse(args);
        assertEquals(OrderMethod.FILE, orderArgumentCaptor.getValue().getOrderMethod());
    }

    @ParameterizedTest
    @ValueSource(strings = {"--verify", "-v"})
    void parseVerify(String command) {
        String[] args = new String[]{command, "-h " + host, "-p " + port};
        commandLineHandler.parse(args);
        assertEquals(OrderMethod.STATUS, orderArgumentCaptor.getValue().getOrderMethod());
    }

    @ParameterizedTest
    @ValueSource(strings = {"--remove", "-rm"})
    void parseRemove(String command) {
        String[] args = new String[]{command, "-h " + host, "-p " + port, "-f /tmp/file.txt"};
        commandLineHandler.parse(args);
        assertEquals(OrderMethod.REMOVE, orderArgumentCaptor.getValue().getOrderMethod());
    }

}