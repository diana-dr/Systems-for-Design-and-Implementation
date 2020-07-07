package application.Service;

import application.Domain.Book;
import application.Domain.Validators.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Set;

public interface IBookService extends IEntityService<Book> {
    Set<Book> filterBooksByPrice(int price);
    Set<Book> filterBooksByAuthor(String author);
    void updateBook(Book book);
}
