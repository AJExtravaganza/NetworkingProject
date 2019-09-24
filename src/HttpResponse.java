import java.util.Date;

public class HttpResponse {
    String version = "HTTP/1.1";
    String statusCode = "200";
    String statusDescription = "OK";
    String contentType = "text/html; charset=utf-8";
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

        return headers + separator + content + '\n';
    }

    String badRequest() {
        statusCode = "400";
        statusDescription = "Bad Request";
        setContent("<h1>Invalid Request</h1>");
        return get();
    }
}
