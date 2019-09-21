import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        Server server = new Server(5000);
        server.main(null);
    }
}
