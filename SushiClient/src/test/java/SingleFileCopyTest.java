import com.sushi.client.Client;
import org.junit.Test;

public class SingleFileCopyTest {

    @Test
    public void push() {
        String[] args = new String[]{"--backup", "-h localhost", "-p 9444", "-f /tmp/input/test.txt:/tmp/output"};
        Client.main(args);
    }

    @Test
    public void pull() {
        String[] args = new String[]{"--fetch", "-h localhost", "-p 9444", "-f /tmp/input/test.txt"};
        Client.main(args);
    }

    @Test
    public void testFile() {
        String[] args = new String[]{"--list", "-h localhost", "-p 9443", "-f /tmp/output"};
        Client.main(args);
    }

    @Test
    public void testRemove() {
        String[] args = new String[]{"--remove", "-h localhost", "-p 9444", "-f /tmp/output/test.txt"};
        Client.main(args);
    }

    @Test
    public void testChecksum() {
        String[] args = new String[]{"--verify", "-h localhost", "-p 9443"};
        Client.main(args);
    }

}