public class HttpResponse {
    String httpVersion = "HTTP/1.1";
    String statusCode = "200";
    String statusDescription = "OK";

    public HttpResponse() {

    }

    public String get() {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(httpVersion).append("\r\n")
                .append(statusCode).append("\r\n")
                .append(statusDescription).append("\r\n");

        strBuild.append("\r\n");

        strBuild.append("message contents");
        return strBuild.toString();
    }

    public String getExample() {
        return "HTTP/1.1 200 OK\r\n" +
                "Date: Mon, 27 Jul 2009 12:28:53 GMT\r\n" +
                "Server: Apache/2.2.14 (Win32)\r\n" +
                "Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT\r\n" +
                "Content-Length: 88\r\n" +
                "Content-Type: text/html\r\n" +
                "Connection: Closed";
    }
}
