package Repository;

import Domain.BaseEntity;
import Domain.Book;
import Domain.Validators.BookValidator;
import Domain.Validators.Validator;
import Repository.FileRepository.BookFileRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class BookFileRepositoryTest extends BaseEntity {

    Validator<Book> validator;
    BookFileRepository memoryRepository;
    String fileName;
    Book book1;
    Book book2;

    @Before
    public void setUp() {
        fileName = "./data/books.txt";
        validator = new BookValidator();
        memoryRepository = new BookFileRepository(validator, fileName);

        Book book1 = new Book("porn","testTitle","testAuthor","02.02.2009",100);
        book1.setId(3L);
        Book book2 = new Book("porn","testTitle","testAuthor","02.02.2009",100);
        book1.setId(3L);
    }

    @Test
    public void save() throws Exception {
        memoryRepository.save(book1);
        Optional<Book> opt = Optional.ofNullable(memoryRepository.findOne(3L).orElseThrow(() -> new Exception("Not found")));
        opt.ifPresent(book -> assertEquals(book1, book));
    }

    @Test
    public void delete() throws Exception {
        memoryRepository.save(book2);
        memoryRepository.delete(3L);
        assertTrue(!memoryRepository.findOne(3L).isPresent());
    }

    @Test
    public void update() throws Exception {

        memoryRepository.save(book1);
        memoryRepository.update(book2);

        Optional<Book> opt = Optional.ofNullable(memoryRepository.findOne(3L).orElseThrow(() -> new Exception("Not found")));
        opt.ifPresent(book -> assertEquals(book2, book));
    }
}