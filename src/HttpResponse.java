import java.util.Date;

public class HttpResponse {
    String version = "HTTP/1.1";
    String statusCode = "200";
    String statusDescription = "OK";
    String contentType = "text/html";
    String content = "";
    int contentLength = 0;

    public HttpResponse() {

    }

    public void setContent(String content) {
        this.content = content;
        this.contentLength = content.length();
    }

    public String get() {
        String headers = String.format(
                "%s %s %s\r\n" +
                "Date: %s\r\n" +
                "Content-Type: %s\r\n" +
                "Content-Length: %s\r\n",
                version, statusCode, statusDescription,
                new Date().toString(),
                contentType,
                contentLength);
        String separator = "\r\n";

        String response = headers + separator + content + '\n';
        return response;
    }

    public String badRequest() {
        statusCode = "400";
        statusDescription = "Bad Request";
        setContent("<h1>Invalid Request</h1>");
        return get();
    }

    public String getExample() {
        String output = "<html><head><title>Example</title></head><body><p>Worked!!!</p></body></html>";
        String headers = String.format("HTTP/1.1 200 OK\r\n" +
                "Date: %s\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: %s\r\n", new Date().toString(), output.length());
        String separator = "\r\n";

        String response = headers + separator + output + '\n';
        return response;
    }
}
