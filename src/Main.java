import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("time.nist.gov", 13);
        System.out.println("done");

    }
}
