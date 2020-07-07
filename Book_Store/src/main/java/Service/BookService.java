package Service;

import Domain.Book;
import Domain.Validators.ValidatorException;
import Repository.JDBC.Direction;
import Repository.JDBC.Sort;
import Repository.Repository;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BookService  extends EntityService<Book>{

    public BookService(Repository<Long, Book> repo) {
        super(repo);
    }

    public Set<Book> filterBooksByPrice(int price) {
        List<Book> filteredBooks = new ArrayList<>();
        Iterable<Book> books = repository.findAll();

        books.forEach(filteredBooks::add);

        return filteredBooks.stream().filter(book -> book.getPrice() == price).collect(Collectors.toSet());
    }

    public Set<Book> filterBooksByAuthor(String author) {
        List<Book> filteredBooks = new ArrayList<>();
        Iterable<Book> books = repository.findAll();

        books.forEach(filteredBooks::add);

        return filteredBooks.stream().filter(book -> book.getAuthor().equals(author)).collect(Collectors.toSet());
    }

    public Iterable<Book> sort(String direc, String ... args){
        List<Book> list = new ArrayList<>();
        if (direc.equals("ASC")){
            Sort<Book> sort = new Sort<>(Direction.ASC,args);
            return repository.findAll(sort);
        }
        else {
            Sort<Book> sort = new Sort<>(Direction.DESC,args);
            return repository.findAll(sort);
        }
    }
}

