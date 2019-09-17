import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class tester {
    public tester() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(String.format("got input %s", in.readLine()));
    }
}
