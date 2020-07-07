package application.Service;

import application.Domain.Book;
import application.Domain.Client;
import application.Domain.Transaction;
import application.Repository.BookRepository;
import application.Repository.ClientRepository;
import application.Repository.Repository;
import application.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;
import java.util.TreeMap;

@Service
public class TransactionServiceImpl extends EntityServiceImpl<Transaction> implements ITransactionService {

    private static int id = 0;

    @Autowired
    Repository<Long, Book> bookRepository;
    Repository<Long, Client> clientRepository;

    public TransactionServiceImpl(TransactionRepository repository, BookRepository bookRepository, ClientRepository clientRepository) {
        super(repository);
        this.clientRepository = clientRepository;
        this.bookRepository = bookRepository;
    }

    public static synchronized int getID() {
        id += 1;
        return id;
    }

    @Override
    public TreeMap<Integer, Client> sortClients() {

        Iterable<Client> clients = clientRepository.findAll();
        TreeMap<Integer, Client> report = new TreeMap<>();
        Iterable<Transaction> transactions = repository.findAll();
        Iterable<Book> books = bookRepository.findAll();

        for (Client client : clients) {
            int total = 0;
            for (Book book : books) {
                for (Transaction transaction : transactions) {
                    if (transaction.getClientID().equals(client.getId()) && transaction.getBookID().equals(book.getId())) {
                        total += book.getPrice();
                    }
                }
            }
            report.put(total, client);
        }

        return report;
    }

    @Override
    public TreeMap<Integer, Book> sortBooks() {

        Iterable<Book> books = bookRepository.findAll();
        TreeMap<Integer, Book> report = new TreeMap<>();

        Iterable<Transaction> transactions = repository.findAll();

        for (Book book: books) {
            int count = 0;
            for (Transaction transaction : transactions) {
                if (book.getId().equals(transaction.getBookID()))
                    count++;
            }
            report.put(count, book);
        }

        return report;
    }

    @Override
    public void removeRelationClient(long clientId) {
        for (Transaction tr : this.repository.findAll()) {
            if (tr.getClientID() == clientId)
                this.removeEntity(tr.getId());
        }
    }

    @Override
    public void removeRelationBook(long bookID) {
        for (Transaction tr : this.repository.findAll()) {
            if (tr.getBookID() == bookID)
                this.removeEntity(tr.getId());
        }
    }
}


