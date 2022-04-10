import com.sushi.client.cmd.CommandLineHandler;
import com.sushi.client.order.OrderController;
import com.sushi.components.message.serving.ServingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.sushi.components.message.serving.ServingStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class IntegrationTest {

    private static final String LOCAL_DIR = "src/test/resources/";
    private static final String REMOTE_DIR = "/";
    private static final String FILE = "test.txt";

    @Container
    private final static GenericContainer SERVER = new GenericContainer<>(DockerImageName.parse("sdenboer/sushi-server"))
            .withExposedPorts(9443, 9444).withImagePullPolicy(PullPolicy.alwaysPull());


    private String host;
    private int tlsPort;
    private int nonTlsPort;

    private String hostCmd;
    private String tlsPortCmd;
    private String nonTlsPortCmd;

    private CommandLineHandler cmdHandler;

    @BeforeEach
    void setUp() {
        host = SERVER.getHost();
        tlsPort = SERVER.getMappedPort(9443);
        nonTlsPort = SERVER.getMappedPort(9444);
        hostCmd = "-h " + host;
        tlsPortCmd = "-p " + tlsPort;
        nonTlsPortCmd = "-p " + nonTlsPort;
        cmdHandler = new CommandLineHandler(new OrderController(tlsPort));
    }

    @Test
    void testPush() {
        String[] args = new String[]{"--backup", hostCmd, tlsPortCmd, "-f " + LOCAL_DIR + FILE + ":" + REMOTE_DIR,};
        ServingStatus servingStatus = cmdHandler.parse(args);
        assertEquals(OK, servingStatus);
    }

    @Test
    void testRemove() {
        testPush();
        System.out.println("DONE");
        String[] args = new String[]{"--remove", "-f " + REMOTE_DIR + FILE, hostCmd, tlsPortCmd};
        ServingStatus servingStatus = cmdHandler.parse(args);
        assertEquals(OK, servingStatus);
    }

    @Test
    void testPull() {
        testPush();
        String[] args = new String[]{"--fetch", "-f " + REMOTE_DIR + FILE, hostCmd, tlsPortCmd};
        ServingStatus servingStatus = cmdHandler.parse(args);
        assertEquals(OK, servingStatus);
    }

    @Test
    void testStatus() {
        testPush();
        String[] args = new String[]{"--verify", hostCmd, tlsPortCmd};
        ServingStatus servingStatus = cmdHandler.parse(args);
        assertEquals(OK, servingStatus);
    }

    @Test
    void testFile() {
        testPush();
        String[] args = new String[]{"--list", "-f " + REMOTE_DIR, hostCmd, tlsPortCmd};
        ServingStatus servingStatus = cmdHandler.parse(args);
        assertEquals(OK, servingStatus);
    }

}