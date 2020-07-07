package socket.server.Service;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import socket.common.ITransactionService;
import socket.server.Domain.Book;
import socket.server.Domain.Client;
import socket.server.Domain.Transaction;
import socket.server.Domain.Validators.ValidatorException;
import socket.server.Repository.JDBC.Direction;
import socket.server.Repository.Repository;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class TransactionService extends EntityService<Transaction> implements ITransactionService {
    private static int id = 0;

    Repository<Long, Book> bookRepository;
    Repository<Long, Client> clientRepository;

    public TransactionService(Repository<Long, Transaction> repo, Repository<Long, Book> bookRepository, Repository<Long, Client> clientRepository, ExecutorService executorService) {
        super(repo, executorService);
        this.clientRepository = clientRepository;
        this.bookRepository = bookRepository;
        entityClass = Transaction.class;
    }

    public static synchronized int getID() {
        id += 1;
        return id;
    }

    @Override
    public Future<String> addEntity(String values) throws ValidatorException {return executorService.submit(()->{
            try {
                Transaction transaction = new Transaction(values);
                clientRepository.findOne(transaction.getClientID()).orElseThrow(() -> new ValidatorException("Missing client with this id!"));
                bookRepository.findOne(transaction.getBookID()).orElseThrow(() -> new ValidatorException("Missing book with this id!"));
                repository.save(transaction);
                return "transaction added";
            } catch (Exception e)
            {
                throw new ValidatorException(e.getMessage());
            }
        });
    }

    public Future<String> sortClients() {
        return executorService.submit(()-> {
            JSONArray toReturn = new JSONArray();
            List<Transaction> sortedTransactions = new ArrayList<>();
            Iterable<Client> clients = clientRepository.findAll();
            Iterable<Transaction> transactions = repository.findAll();
            TreeMap<Integer, Client> report = new TreeMap<>();

            transactions.forEach(sortedTransactions::add);

            clients.forEach(client -> {
                int total = sortedTransactions.stream()
                    .filter(transaction -> transaction.getClientID().equals(client.getId()))
                    .mapToInt(transaction -> bookRepository.findOne(transaction.getBookID()).map(Book::getPrice).orElse(0))
                    .sum();
                report.put(total, client);

            });

            report.forEach((total, client) -> {

                JSONObject object = new JSONObject();
                object.put("total", total);
                object.put("id", client.getId());
                object.put("firstName", client.getFirstName());
                object.put("lastName", client.getLastName());
                object.put("dateOfBirth", client.getDateOfBirth());
                object.put("email", client.getEmail());

            });

            return toReturn.toString();
            }
        );
    }

    public Future<String> sortBooks() {
        return executorService.submit(()-> {
            JSONArray toReturn = new JSONArray();
            Iterable<Book> books = bookRepository.findAll();
            Iterable<Transaction> transactions = repository.findAll();
            TreeMap<Integer, Book> report = new TreeMap<>();

            for (Book book: books) {
                int count = 0;
                for (Transaction transaction : transactions) {
                    if (book.getId().equals(transaction.getBookID()))
                        count++;

                    report.put(count, book);
                }
            }

            report.forEach((count, book) -> {
                JSONObject object = new JSONObject();
                object.put("count", count);
                object.put("id", book.getId());
                object.put("category", book.getCategory());
                object.put("title", book.getTitle());
                object.put("author", book.getAuthor());
                object.put("date", book.getReleaseDate());
                object.put("price", book.getPrice());
                toReturn.put(object);
            });

            return toReturn.toString();
            }
        );
}

    public Future<String> removeRelationClient(String string) {
        return executorService.submit(() -> {
            try{
                JSONTokener tokener = new JSONTokener(string);
                JSONObject object = new JSONObject(tokener);

                Long client_id = object.getLong("id");
                for (Transaction tr : this.repository.findAll()) {

                    if (tr.getClientID().equals(client_id)) {
                        JSONObject object1 = new JSONObject();
                        object1.put("id", tr.getId());
                        this.removeEntity(object1.toString());
                    }
                }
                return "entity deleted";

            } catch (Exception e){
                throw new ValidatorException(e.getMessage());
            }
        });
    }

    public Future<String> removeRelationBook(String string) {
        return executorService.submit(() -> {
            try {
                JSONTokener tokener = new JSONTokener(string);
                JSONObject object = new JSONObject(tokener);

                Long book_id = object.getLong("id");
                for (Transaction tr : this.repository.findAll()) {

                    if (tr.getClientID().equals(book_id)) {
                        JSONObject object1 = new JSONObject();
                        object1.put("id", tr.getId());
                        this.removeEntity(object1.toString());
                    }
                }
                return "entity deleted";

            } catch (Exception e){
                throw new ValidatorException(e.getMessage());
            }
        });
    }
}

