package socket.client.Service;


import socket.client.TCP.TcpClient;
import socket.common.IBookService;
import socket.common.ITransactionService;
import socket.common.Request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TransactionService extends EntityService implements ITransactionService {

    ExecutorService executorService;
    TcpClient tcpClient;

    public TransactionService(ExecutorService executorService, TcpClient tcpClient) {
        super(executorService, tcpClient);
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<String> sortClients() {
        return executorService.submit(() -> {
            Request request = new Request(ITransactionService.SORT_CLIENTS, null);
            System.out.println("sending request: "+ request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }

    @Override
    public Future<String> sortBooks() {
        return executorService.submit(() -> {
            Request request = new Request(ITransactionService.SORT_BOOKS, null);
            System.out.println("sending request: "+ request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }

    @Override
    public Future<String> removeRelationClient(String toString) {
        return executorService.submit(() -> {
            Request request = new Request(ITransactionService.REMOVE_RELATION_CLIENT, toString);
            System.out.println("sending request: "+ request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }

    @Override
    public Future<String> removeRelationBook(String toString) {
        return executorService.submit(() -> {
            Request request = new Request(ITransactionService.REMOVE_RELATION_BOOK, toString);
            System.out.println("sending request: "+ request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }
}
