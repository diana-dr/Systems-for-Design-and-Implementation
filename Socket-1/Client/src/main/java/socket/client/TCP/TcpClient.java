package socket.client.TCP;

import socket.common.Request;
import socket.common.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {
    public Request sendAndReceive(Request request) {
        try (Socket socket = new Socket(Request.HOST, Request.PORT);
             InputStream is = socket.getInputStream();
             OutputStream os = socket.getOutputStream()
        ) {
            System.out.println("sendAndReceive - sending request: " + request);
            request.writeTo(os);

            System.out.println("sendAndReceive - received response: ");
            Request response = new Request();
            response.readFrom(is);
            System.out.println(response);

            return response;
        } catch (IOException e) {
            throw new ServiceException("error connection to server " + e.getMessage(), e);
        }
    }
}
