import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class SimpleHttpRequest {
    String method = null;
    URI uri = null;
    String version = null;
    String dumpedHeaders = null;
    public SimpleHttpRequest(InputStream inputStream) throws IOException, URISyntaxException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder headerDump = new StringBuilder();
        String line = in.readLine();
        String[] status_elements = line.split(" ");
        method = status_elements[0];
        uri = new URI(status_elements[1]);
        version = status_elements[2];

        System.out.println("Received:");
        line = in.readLine();
        try {
            //Dump all this stuff since we're not bothering to pass optional headers
            while (!line.isEmpty()) {
                System.out.println(line);
                line = in.readLine();
                headerDump.append(line);
            }
        } catch (NullPointerException err) {
            System.out.println(line);
            System.out.println("End of request reached");
        }
        dumpedHeaders = headerDump.toString();
    }
}
