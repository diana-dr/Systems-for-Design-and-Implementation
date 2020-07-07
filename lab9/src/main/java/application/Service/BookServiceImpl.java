package application.Service;

import application.Domain.Book;
import application.Repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends EntityServiceImpl<Book> implements IBookService {

    public static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    public BookServiceImpl(BookRepository repo) {
        super(repo);
    }

    @Override
    public Set<Book> filterBooksByPrice(int price) {
        List<Book> filteredBooks = new ArrayList<>();
        Iterable<Book> books = repository.findAll();

        books.forEach(filteredBooks::add);

        return filteredBooks.stream().filter(book -> book.getPrice() == price).collect(Collectors.toSet());
    }

    @Override
    public Set<Book> filterBooksByAuthor(String author) {
        List<Book> filteredBooks = new ArrayList<>();
        Iterable<Book> books = repository.findAll();

        books.forEach(filteredBooks::add);

        return filteredBooks.stream().filter(book -> book.getAuthor().equals(author)).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void updateBook(Book book) {
        log.trace("updateBook - method entered: book={}", book);
        repository.findById(book.getId())
                .ifPresent(b -> {
                    b.setCategory(book.getCategory());
                    b.setAuthor(book.getAuthor());
                    b.setTitle(book.getTitle());
                    b.setCategory(book.getCategory());
                    b.setPrice(book.getPrice());
                    b.setReleaseDate(book.getReleaseDate());
                    log.debug("updateBook - updated: b={}", b);
                });
        log.trace("updateBook - method finished");
    }
}


