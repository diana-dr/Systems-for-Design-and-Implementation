package socket.server;

import socket.common.*;
import socket.server.Domain.BaseEntity;
import socket.server.Domain.Book;
import socket.server.Domain.Client;
import socket.server.Domain.Transaction;
import socket.server.Domain.Validators.BookValidator;
import socket.server.Domain.Validators.ClientValidator;
import socket.server.Domain.Validators.TransactionValidator;
import socket.server.Domain.Validators.Validator;
import socket.server.Repository.JDBC.DB.BookDBRepository;
import socket.server.Repository.JDBC.DB.ClientDBRepository;
import socket.server.Repository.JDBC.DB.TransactionDBRepository;
import socket.server.Repository.Repository;
import socket.server.Service.BookService;
import socket.server.Service.ClientService;
import socket.server.Service.EntityService;
import socket.server.Service.TransactionService;
import socket.server.TCP.TcpServer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerApp {
    public static void main(String[] args) {
        try {
            System.out.println("server started");
            ExecutorService executorService = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors()
            );

            Validator<Book> bookValidator = new BookValidator();
            Repository<Long, Book> booksRepo = new BookDBRepository(bookValidator);
            IBookService bookService = new BookService(booksRepo, executorService);

            Validator<Client> clientValidator = new ClientValidator();
            Repository<Long, Client> clientsRepo = new ClientDBRepository(clientValidator);
            IClientService clientService = new ClientService(clientsRepo, executorService);

            Validator<Transaction> transactionValidator = new TransactionValidator();
            Repository<Long, Transaction> transactionsRepo = new TransactionDBRepository(transactionValidator);
            ITransactionService transactionService = new TransactionService(transactionsRepo, booksRepo, clientsRepo, executorService);

            TcpServer tcpServer = new TcpServer(executorService);

            // CRUD for entities

            tcpServer.addHandler(EntityService.ADD_ENTITY, (request) -> {
                String name = request.getBody();
                Future<String> future = ServerApp.checkEntity(name, bookService, clientService, transactionService).addEntity(name);
                try {
                    String result = future.get();
                    return new Request("ok", result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }

            });

            tcpServer.addHandler(EntityService.REMOVE_ENTITY, (request) -> {
                String name = request.getBody();
                Future<String> future = ServerApp.checkEntity(name, bookService, clientService, transactionService).removeEntity(name);
                try {
                    String result = future.get();
                    return new Request("ok", result);
                } catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }
            });

            tcpServer.addHandler(EntityService.UPDATE_ENTITY, (request) -> {
                String name = request.getBody();
                Future<String> future = ServerApp.checkEntity(name, bookService, clientService, transactionService).updateEntity(name);
                try {
                    String result = future.get();
                    return new Request("ok", result);
                }catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }
            });

            tcpServer.addHandler(EntityService.GET_ENTITIES, (request) -> {
                String name = request.getBody();
                Future<String> future = ServerApp.checkEntity(name, bookService, clientService, transactionService).getAllEntities();
                try {
                    String result = future.get();
                    return new Request("ok", result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }

            });

            // Books

            tcpServer.addHandler(BookService.PRINT_SORTED_BOOKS, (request) -> {
                String name = request.getBody();
                Future<String> future = bookService.printSortedBooks(name);
                try {
                    String result = future.get();
                    return new Request("ok", result);
                }catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }
            });

            // Clients

            tcpServer.addHandler(BookService.PRINT_SORTED_CLIENTS, (request) -> {
                String name = request.getBody();
                Future<String> future = clientService.printSortedClients(name);
                try {
                    String result = future.get();
                    return new Request("ok", result);
                }catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }
            });

            // Transactions

            tcpServer.addHandler(TransactionService.REMOVE_RELATION_CLIENT, (request) -> {
                String name = request.getBody();
                Future<String> future = transactionService.removeRelationClient(name);
                try {
                    String result = future.get();
                    return new Request("ok", result);
                } catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }
            });

            tcpServer.addHandler(TransactionService.REMOVE_RELATION_BOOK, (request) -> {
                String name = request.getBody();
                Future<String> future = transactionService.removeRelationClient(name);
                try {
                    String result = future.get();
                    return new Request("ok", result);
                } catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }
            });

            // -------------------------- FILTERS  AND REPORTS ------------------------
            tcpServer.addHandler(BookService.FILTER_BOOKS_BY_PRICE, (request) -> {
                String name = request.getBody();
                Future<String> future = bookService.filterBooksByPrice(name);
                try {
                    String result = future.get();
                    return new Request("ok", result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }

            });

            tcpServer.addHandler(BookService.FILTER_BOOKS_BY_AUTHOR, (request) -> {
                String name = request.getBody();
                Future<String> future = bookService.filterBooksByAuthor(name);
                try {
                    String result = future.get();
                    return new Request("ok", result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }

            });

            tcpServer.addHandler(TransactionService.SORT_CLIENTS, (request) -> {
                String name = request.getBody();
                Future<String> future = transactionService.sortClients();
                try {
                    String result = future.get();
                    return new Request("ok", result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }

            });

            tcpServer.addHandler(TransactionService.SORT_BOOKS, (request) -> {
                String name = request.getBody();
                Future<String> future = transactionService.sortBooks();
                try {
                    String result = future.get();
                    return new Request("ok", result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return new Request("error", e.getMessage());
                }

            });

            tcpServer.startServer();

            executorService.shutdown();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    public static IEntityService checkEntity(String name, IBookService bookService, IClientService clientService, ITransactionService transactionService) {
        System.out.println(name);
        return name.contains("Book") ? bookService
                : name.contains("Client") ? clientService
                : transactionService;
    }
}
