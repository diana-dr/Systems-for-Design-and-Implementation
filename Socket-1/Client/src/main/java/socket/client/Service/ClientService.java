package socket.client.Service;

import socket.client.TCP.TcpClient;
import socket.common.IClientService;
import socket.common.Request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ClientService extends EntityService implements IClientService {
    public ClientService(ExecutorService executorService, TcpClient tcpClient) {
        super(executorService, tcpClient);
    }

    @Override
    public Future<String> printSortedClients(String entity) {
        return executorService.submit(() -> {
            Request request = new Request(BookService.PRINT_SORTED_CLIENTS, entity);
            System.out.println("sending request: "+request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }
}