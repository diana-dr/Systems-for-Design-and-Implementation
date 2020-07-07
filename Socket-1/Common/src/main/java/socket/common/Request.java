package socket.common;

import java.io.*;

public class Request {
    public static final int PORT = 1234;
    public static final String HOST = "localhost";

    private String header;
    private String body;

    public Request() {
    }

    public Request(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void writeTo(OutputStream os) throws IOException {
        os.write((header + System.lineSeparator() + body + System.lineSeparator()).getBytes());
    }

    public void readFrom(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        header = br.readLine();
        body = br.readLine();
    }

    @Override
    public String toString() {
        return "Request {" +
                "header='" + header + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
