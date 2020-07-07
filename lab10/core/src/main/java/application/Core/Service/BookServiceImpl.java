package application.Core.Service;


import application.Core.Domain.Book;
import application.Core.Domain.Validators.BookValidator;
import application.Core.Repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends EntityServiceImpl<Book> implements IBookService {

    public static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    public BookValidator validator = new BookValidator();

    @Autowired
    public BookServiceImpl(BookRepository repo) {
        super(repo);
    }

    @Override
    public Set<Book> filterBooksByPrice(int price) {
        log.trace("filterBooksByPrice() --- method entered price {}", price);
        List<Book> filteredBooks = new ArrayList<>();
        Iterable<Book> books = repository.findAll();
        books.forEach(filteredBooks::add);
        Set<Book> result = filteredBooks.stream().filter(book -> book.getPrice() == price).collect(Collectors.toSet());
        log.trace("filterBooksByPrice() --- method finished books={}", result);

        return result;
    }

    @Override
    public Set<Book> filterBooksByAuthor(String author) {
        log.trace("filterBooksByAuthor() --- method entered author {}", author);
        List<Book> filteredBooks = new ArrayList<>();
        Iterable<Book> books = repository.findAll();
        books.forEach(filteredBooks::add);
        Set<Book> result = filteredBooks.stream().filter(book -> book.getAuthor().equals(author)).collect(Collectors.toSet());
        log.trace("filterBooksByAuthor() --- method finished result {}", result);
        return result;
    }

    @Override
    @Transactional
    public Book updateBook(Long id, Book book) {
        log.trace("updateBook() --- method entered: book={}", book);
        validator.validate(book);
        Book update = repository.findById(id).orElse(book);
        update.setCategory(book.getCategory());
        update.setAuthor(book.getAuthor());
        update.setTitle(book.getTitle());
        update.setCategory(book.getCategory());
        update.setPrice(book.getPrice());
        update.setReleaseDate(book.getReleaseDate());
        log.trace("updateBook() --- method finished");
        return update;
    }
}


