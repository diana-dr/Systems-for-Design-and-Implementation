package application.Core.Service;

import application.Core.Model.Book;
import application.Core.Model.Client;
import application.Core.Model.Transaction;
import application.Core.Repository.BookRepository;
import application.Core.Repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Set<Transaction> getAllTransactionsForClient(Long clientID) {
        log.trace("getAllTransactionsForClient --- method entered");
        Optional<Client> client = clientRepository.findById(clientID);
        Set<Transaction> transactions = new HashSet<>();
        if(client.isPresent())
            transactions = client.get().getClientTransactions();
        log.trace("getAllTransactionsForClient --- method exit, transactions {}", transactions);
        return transactions;
    }

    @Override
    public void addTransaction(Long clientID, Long bookID) {
        log.trace("addTransaction --- method entered! - clientID {}, bookID {}", clientID, bookID);
        Optional<Book> book = bookRepository.findById(bookID);
        Optional<Client> client = clientRepository.findById(clientID);
        if(book.isPresent() && client.isPresent())
            book.get().addClient(client.get());
        log.trace("addTransaction: method exit");
    }

    @Override
    public void updateTransaction(Long clientID, Long bookID) {
    }

    @Transactional
    public void deleteTransaction(Long clientID, Long bookID) {
        log.trace("deleteTransaction --- method entered! - bookID {}, clientID {}", bookID, clientID);
        bookRepository.findById(bookID).ifPresent(book ->
                book.getBookTransactions().stream()
                        .filter(rental -> rental.getClient().getId().equals(clientID))
                        .forEach(movieRental -> book.getBookTransactions().remove(movieRental))
        );
        log.trace("deleteTransaction: exit");
    }
}
