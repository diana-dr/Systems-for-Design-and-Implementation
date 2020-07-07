package application.Service;

import application.Domain.Book;
import application.Domain.Client;
import application.Domain.Transaction;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.TreeMap;

public interface ITransactionService extends IEntityService<Transaction> {
    TreeMap<Integer, Client> sortClients();
    TreeMap<Integer, Book> sortBooks();
    void removeRelationClient(long clientId);
    void removeRelationBook(long bookID);
}
