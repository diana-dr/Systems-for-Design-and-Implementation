package socket.client.Service;

import socket.client.TCP.TcpClient;
import socket.common.IBookService;
import socket.common.Request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class BookService extends EntityService implements IBookService {

    private ExecutorService executorService;
    private TcpClient tcpClient;

    public BookService(ExecutorService executorService, TcpClient tcpClient) {
        super(executorService, tcpClient);
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<String> filterBooksByPrice(String price) {
        return executorService.submit(() -> {
            Request request = new Request(IBookService.FILTER_BOOKS_BY_PRICE, price);
            System.out.println("sending request: "+ request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }

    @Override
    public Future<String> printSortedBooks(String entity) {
        return executorService.submit(() -> {
            Request request = new Request(BookService.PRINT_SORTED_BOOKS, entity);
            System.out.println("sending request: "+request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }

    public Future<String> filterBooksByAuthor(String toString) {
        return executorService.submit(() -> {
            Request request = new Request(IBookService.FILTER_BOOKS_BY_AUTHOR, toString);
            System.out.println("sending request: "+ request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }
}
