package application.Web.Controller;

import application.Core.Model.Book;
import application.Core.Service.IBookService;
import application.Web.Converter.BookConverter;
import application.Web.Dto.BookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
public class BookController {
    public static final Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private IBookService bookService;

    @Autowired
    private BookConverter bookConverter;

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    Set<BookDto> getBooks() {
        log.trace("getBooks() --- method entered");
        Set<Book> bookList = bookService.getAllBooks();
        Set<BookDto> bookDtos = new HashSet<>(bookConverter.convertModelsToDtos(bookList));
        log.trace("getBooks() --- method finished result={}", bookDtos);

        return bookDtos;
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    BookDto getBook(@PathVariable final Long id) {
        log.trace("getBook() --- method entered clientId={}", id);
        Book book = bookService.getBookByID(id);
        BookDto result = bookConverter.convertModelToDto(book);
        log.trace("getBook() --- method finished result={}", book);
        return result;
    }

    @RequestMapping(value = "/books", method = RequestMethod.POST)
    BookDto saveBook(@RequestBody BookDto bookDto) {
        log.trace("saveBook() - method entered: book={}", bookDto);
        BookDto book = bookConverter.convertModelToDto(
                bookService.addBook(bookConverter.convertDtoToModel(bookDto)));
        log.trace("saveBook() - method finished: book={}", book);
        return book;
    }

    @RequestMapping(value = "/books/sorted/{direction}/{args}", method = RequestMethod.GET)
    Set<BookDto> sort(@PathVariable String direction, @PathVariable String args) {
        log.trace("sort() -- method entered: direction={}, args={}", direction, args);

        Set<Book> bookList = bookService.sort(direction, args);

        Set<BookDto> bookDtos = new HashSet<>(bookConverter.convertModelsToDtos(bookList));

        log.trace("sort() -- method finished books {}", bookDtos);
        return bookDtos;
    }

    @RequestMapping(value = "/books/{author}", method = RequestMethod.GET)
    Set<BookDto> filterBooksByAuthor(@PathVariable final String author) {
        log.trace("filterBooksByAuthor() --- method entered author {}", author);
        Set<Book> bookList = bookService.filterBooksByAuthor(author);

        Set<BookDto> bookDtos = new HashSet<>(bookConverter.convertModelsToDtos(bookList));

        log.trace("filterBooksByAuthor() --- method finished result {}", bookDtos);
        return bookDtos;
    }

    @RequestMapping(value = "/books/filtered/{price}", method = RequestMethod.GET)
    Set<BookDto> filterBooksByPrice(@PathVariable final int price) {
        log.trace("filterBooksByPrice() --- method entered price {}", price);
        Set<Book> bookList = bookService.filterBooksByPrice(price);

        Set<BookDto> bookDtos = new HashSet<>(bookConverter.convertModelsToDtos(bookList));
        log.trace("filterBooksByPrice() --- method finished result {}", bookDtos);
        return bookDtos;
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.PUT)
    BookDto updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        log.trace("updateBook() - method entered: book={}", bookDto);
        BookDto book = bookConverter.convertModelToDto( bookService.updateBook(id,
                bookConverter.convertDtoToModel(bookDto)));
        log.trace("updateBook() - method finished");
        return book;
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteBook(@PathVariable Long id){
        log.trace("deleteBook() - method entered: bookID={}", id);
        bookService.removeBook(id);
        log.trace("deleteBook() - method finished");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
