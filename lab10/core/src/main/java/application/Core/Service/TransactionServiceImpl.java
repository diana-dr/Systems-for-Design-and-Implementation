package application.Core.Service;


import application.Core.Domain.Book;
import application.Core.Domain.Client;
import application.Core.Domain.Transaction;
import application.Core.Repository.BookRepository;
import application.Core.Repository.ClientRepository;
import application.Core.Repository.Repository;
import application.Core.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
public class TransactionServiceImpl extends EntityServiceImpl<Transaction> implements ITransactionService {

    @Autowired
    Repository<Long, Book> bookRepository;
    Repository<Long, Client> clientRepository;

    public TransactionServiceImpl(TransactionRepository repository, BookRepository bookRepository, ClientRepository clientRepository) {
        super(repository);
        this.clientRepository = clientRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void removeRelationClient(long clientId) {
        log.trace("removeRelationClient() --- method entered: clientID={}", clientId);
        for (Transaction tr : this.repository.findAll()) {
            if (tr.getClientID() == clientId)
                this.removeEntity(tr.getId());
        }
        log.trace("removeRelationClient() --- method finished");
    }

    @Override
    public void removeRelationBook(long bookID) {
        log.trace("removeRelationBook() --- method entered: bookID={}", bookID);
        for (Transaction tr : this.repository.findAll()) {
            if (tr.getBookID() == bookID)
                this.removeEntity(tr.getId());
        }
        log.trace("removeRelationBook() --- method finished");
    }
}


