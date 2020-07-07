package Service;
import Domain.Book;
import Domain.Client;
import Domain.Transaction;
import Domain.Validators.ValidatorException;
import Repository.Repository;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.TreeMap;

public class TransactionService extends EntityService<Transaction> {
    private static int id = 0;

    Repository<Long, Book> bookRepository;
    Repository<Long, Client> clientRepository;

    public TransactionService(Repository<Long, Transaction> repo, Repository<Long, Book> bookRepository, Repository<Long, Client> clientRepository) {
        super(repo);
        this.clientRepository = clientRepository;
        this.bookRepository = bookRepository;
    }

    public static synchronized int getID() {
        id += 1;
        return id;
    }

    @Override
    public void addEntity(Transaction transaction) throws ParserConfigurationException, IOException, SAXException, TransformerException, ValidatorException {
        clientRepository.findOne(transaction.getClientID()).orElseThrow(() -> new ValidatorException("Missing client with this id!"));
        bookRepository.findOne(transaction.getBookID()).orElseThrow(() -> new ValidatorException("Missing book with this id!"));
        repository.save(transaction);
    }

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

    public void removeRelationClient(long clientId) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        for (Transaction tr : this.repository.findAll()) {
            if (tr.getClientID() == clientId)
                this.removeEntity(tr.getId());
        }
    }

    public void removeRelationBook(long bookID) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        for (Transaction tr : this.repository.findAll()) {
            if (tr.getBookID() == bookID)
                this.removeEntity(tr.getId());
        }
    }
}

