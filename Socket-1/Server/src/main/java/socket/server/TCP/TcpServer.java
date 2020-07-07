package socket.server.TCP;

import socket.common.Request;
import socket.common.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

public class TcpServer {
    private ExecutorService executorService;
    private Map<String, UnaryOperator<Request>> methodHandlers;

    public TcpServer(ExecutorService executorService) {
        this.methodHandlers = new HashMap<>();
        this.executorService = executorService;
    }

    public void addHandler(String methodName, UnaryOperator<Request> handler) {
        methodHandlers.put(methodName, handler);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(Request.PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                executorService.submit(new ClientHandler(client));
            }
        } catch (IOException e) {
            throw new ServiceException("error connecting clients", e);
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket client) {
            this.socket = client;
        }

        @Override
        public void run() {
            try (InputStream is = socket.getInputStream();
                 OutputStream os = socket.getOutputStream()) {
                Request request = new Request();
                request.readFrom(is);
                System.out.println("received request: " + request);

                Request response = methodHandlers.get(request.getHeader())
                        .apply(request);
                response.writeTo(os);

            } catch (IOException e) {
                throw new ServiceException("error processing client", e);
            }
        }
    }
}
