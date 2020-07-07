package Repository;

import Domain.Book;
import Domain.Validators.BookValidator;
import Domain.Validators.Validation;
import Domain.Validators.Validator;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class InMemoryRepositoryTest {

    private Validator<Book> validator;
    Book b1;
    Book b2;
    Repository<Long,Book> repo;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        validator = new BookValidator();
        repo = new InMemoryRepository<Long, Book>(validator);
        b1 = new Book("cteg1","trrr1","arrr1","12.12.1212",22);
        b2 = new Book("cteg2","trrr2","arrr2","12.12.1212",22);
        b1.setId(1L);
        b2.setId(2L);
        repo.save(b1);
        repo.save(b2);
    }

    @Test
    public void findOne() throws Exception {
        Optional<Book> e = Optional.ofNullable(repo.findOne(1L).orElseThrow((() -> new Exception("not found"))));
        e.ifPresent(book -> assertEquals(b1, book));
    }

    @Test
    public void findAll() {
        HashSet<Book> entities = new HashSet<>();
        entities.add(b1);
        entities.add(b2);
        assertEquals(entities,repo.findAll());
    }

    @Test
    public void save() throws Exception {
        Book b3 = new Book("categ3", "trrr3","arrr3", "12.12.1221",55);
        b3.setId(3L);
        repo.save(b3);
        Optional<Book> e = Optional.ofNullable(repo.findOne(3L).orElseThrow((() -> new Exception("not found"))));
        e.ifPresent(book -> assertEquals(b3, book));
    }

    @Test
    public void delete() throws Exception {
        Book b3 = new Book("categ3", "trrr3","arrr3", "12.12.1221",55);
        b3.setId(3L);
        repo.save(b3);
        repo.delete(3L);
        assertEquals(true, !repo.findOne(3L).isPresent());
    }

    @Test
    public void update() {
        Book b4 = new Book("diana","diana","diana","12.12.1212",22);
        b4.setId(2L);
        repo.update(b4);
        assertEquals(b4.getAuthor(),"diana");
    }
}