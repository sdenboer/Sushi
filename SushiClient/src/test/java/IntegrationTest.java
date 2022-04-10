import com.sushi.client.cmd.CommandLineHandler;
import com.sushi.components.message.serving.ServingStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.sushi.components.message.serving.ServingStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
class IntegrationTest {

    @ParameterizedTest
    @ValueSource(ints = {9444, 9443})
    void testPush(int port) {
        String[] args = new String[]{"--backup", "-h localhost", "-p " + port,
                "-f /tmp/input/test.txt:/tmp/output"};
        ServingStatus servingStatus = new CommandLineHandler().parse(args);
        assertEquals(OK, servingStatus);
    }

    @ParameterizedTest
    @ValueSource(ints = {9444, 9443})
    void testPull(int port) {
        testPush(port);
        String[] args = new String[]{"--fetch", "-h localhost", "-p " + port,
                "-f /tmp/output/test.txt"};
        ServingStatus servingStatus = new CommandLineHandler().parse(args);
        assertEquals(OK, servingStatus);
    }

    @ParameterizedTest
    @ValueSource(ints = {9444, 9443})
    void testFile(int port) {
        testPush(port);
        String[] args = new String[]{"--list", "-h localhost", "-p " + port, "-f /tmp/output"};
        ServingStatus servingStatus = new CommandLineHandler().parse(args);
        assertEquals(OK, servingStatus);
    }

    @ParameterizedTest
    @ValueSource(ints = {9444, 9443})
    void testRemove(int port) {
        testPush(port);
        String[] args = new String[]{"--remove", "-h localhost", "-p " + port,
                "-f /tmp/output/test.txt"};
        ServingStatus servingStatus = new CommandLineHandler().parse(args);
        assertEquals(OK, servingStatus);

    }

    @ParameterizedTest
    @ValueSource(ints = {9444, 9443})
    void testChecksum(int port) {
        testPush(port);
        String[] args = new String[]{"--verify", "-h localhost", "-p " + port};
        ServingStatus servingStatus = new CommandLineHandler().parse(args);
        assertEquals(OK, servingStatus);
    }

}