import com.sushi.client.CommandLineHandler;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class IntegrationTest {

    @Test
    public void testPush() {
        String[] args = new String[]{"--backup", "-h localhost", "-p 9443", "-f /tmp/input/test.txt:/tmp/output"};
        CommandLineHandler.parse(args);
    }

    @Test
    public void testPull() {
        String[] args = new String[]{"--fetch", "-h localhost", "-p 9444", "-f /tmp/output/test.txt"};
        CommandLineHandler.parse(args);
    }

    @Test
    public void testFile() {
        String[] args = new String[]{"--list", "-h localhost", "-p 9443", "-f /tmp/output"};
        CommandLineHandler.parse(args);
    }

    @Test
    public void testRemove() {
        String[] args = new String[]{"--remove", "-h localhost", "-p 9444", "-f /tmp/output/test.txt"};
        CommandLineHandler.parse(args);
    }

    @Test
    public void testChecksum() {
        String[] args = new String[]{"--verify", "-h localhost", "-p 9443"};
        CommandLineHandler.parse(args);
    }

}