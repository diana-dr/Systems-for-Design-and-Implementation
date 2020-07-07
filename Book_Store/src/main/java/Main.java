import Domain.Book;
import Domain.Client;
import Domain.Transaction;
import Domain.Validators.BookValidator;
import Domain.Validators.ClientValidator;
import Domain.Validators.TransactionValidator;
import Domain.Validators.Validator;
import Repository.JDBC.DB.BookDBRepository;
import Repository.JDBC.DB.ClientDBRepository;
import Repository.JDBC.DB.TransactionDBRepository;
import Repository.Repository;
import Service.BookService;
import Service.ClientService;
import Service.TransactionService;
import UI.Console;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        Validator<Book> bookValidator = new BookValidator();
        // Repository<Long,Books> booksRepository = new InMemoryRepository<>(bookValidator);
        //Repository<Long, Book> booksRepo = new BookFileRepository(bookValidator, "./data/books.txt");
        //Repository<Long,Book> booksRepo = new BookXMLRepository(bookValidator,"./data/bookstore.xml");
        Repository<Long,Book> booksRepo = new BookDBRepository(bookValidator);
        BookService bookService = new BookService(booksRepo);

        Validator<Client> clientsValidator = new ClientValidator();
        //Repository<Long,Client> clientsRepository = new InMemoryRepository<>(clientsValidator);
        //Repository<Long, Client> clientsRepository = new ClientFileRepository(clientsValidator, "./data/clients.txt");
        //Repository<Long,Client> clientsRepository = new ClientXMLRepository(clientsValidator,"./data/clients.xml");
        Repository<Long,Client> clientsRepository = new ClientDBRepository(clientsValidator);
        ClientService clientService = new ClientService(clientsRepository);

        Validator<Transaction> transactionsValidator = new TransactionValidator();
        //Repository<Long,Transaction> transactionsRepository = new InMemoryRepository<>(transactionsValidator);
        //Repository<Long, Transaction> transactionsRepository = new TransactionFileRepository(transactionsValidator, "./data/transactions.txt");
        Repository<Long,Transaction> transactionRepository = new TransactionDBRepository(transactionsValidator);
        //Repository<Long, Transaction> transactionRepository = new TransactionXMLRepository(transactionsValidator,"./data/transactions.xml");
        TransactionService transactionService = new TransactionService(transactionRepository, booksRepo, clientsRepository);

        try {
           Console console = new Console(bookService, clientService, transactionService);
            console.runConsole();
        } catch (Exception ex) {System.out.println(ex.getMessage());}

    }
}
