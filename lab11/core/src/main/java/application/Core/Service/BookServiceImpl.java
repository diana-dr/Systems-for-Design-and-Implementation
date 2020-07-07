package application.Core.Service;


import application.Core.Model.Book;
import application.Core.Model.Client;
import application.Core.Model.Validators.BookValidator;
import application.Core.Model.Validators.ValidatorException;
import application.Core.Repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements IBookService {

    public static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    public BookValidator validator = new BookValidator();

    @Autowired
    private BookRepository repository;

    public Set<Book> filterBooksByPrice(int price) {
        log.trace("filterBooksByPrice() --- method entered price {}", price);
        List<Book> filteredBooks = new ArrayList<>();
        Iterable<Book> books = repository.findAll();
        books.forEach(filteredBooks::add);
        Set<Book> result = filteredBooks.stream().filter(book -> book.getPrice() == price).collect(Collectors.toSet());
        log.trace("filterBooksByPrice() --- method finished books={}", result);

        return result;
    }

    public Set<Book> filterBooksByAuthor(String author) {
        log.trace("filterBooksByAuthor() --- method entered author {}", author);
        List<Book> filteredBooks = new ArrayList<>();
        Iterable<Book> books = repository.findAll();
        books.forEach(filteredBooks::add);
        Set<Book> result = filteredBooks.stream().filter(book -> book.getAuthor().equals(author)).collect(Collectors.toSet());
        log.trace("filterBooksByAuthor() --- method finished result {}", result);
        return result;
    }

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
        update.setBookTransactions(book.getBookTransactions());
        log.trace("updateBook() --- method finished");
        return update;
    }

    @Override
    public Book addBook(Book entity) {
        log.trace("addEntity - method entered: entity={}", entity);
//        validator.validate(entity);
        repository.save(entity);
        log.trace("addEntity - method finished");
        return entity;
    }

    @Override
    public Set<Book> getAllBooks() {
        log.trace("getAllEntities --- method entered");

        Iterable<Book> entities = repository.findAll();
        Set<Book> entitiesSet = new HashSet<>();
        entities.forEach(entitiesSet::add);

        log.trace("getAllEntities: result={}", entitiesSet);
        return entitiesSet;
    }

    @Override
    public void removeBook(long id) {
        Book entity = getBookByID(id);
        log.trace("deleteEntity() --- method entered: entity={}", entity);
        repository.deleteById(id);
        log.trace("deleteEntity() --- deleted: entity={}", entity);
    }

    @Override
    public Book getBookByID(long id) throws ValidatorException {
        log.trace("getEntityByID() --- method entered: entityID={}", id);
        if (repository.findById(id).isEmpty())
            throw new ValidatorException("Missing entity with ID: " + id);
        Book entity = repository.findById(id).get();
        log.trace("getEntityByID() --- method finished: entity={}", entity);
        return entity;
    }

    @Override
    public Set<Book> sort(String direc, String args) {
        log.trace("sort() --- method entered: direction={}, args={}", direc, args);
        Set<Book> result = new HashSet<>();
        if (direc.equals("ASC")) {
            Iterable<Book> entities = repository.findAll(Sort.by(Sort.Direction.ASC, args));
            entities.forEach(result::add);
        } else if (direc.equals("DESC")) {
            Iterable<Book> entities = repository.findAll(Sort.by(Sort.Direction.DESC, args));
            entities.forEach(result::add); }

        log.trace("sort() --- method finished: result={}", result);
        return result;
    }

    @Override
    public void addTransaction(Client client, Book book) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        book.addTransaction(client, formatter.format(date));
        updateBook(book.getId(), book);
    }
}


