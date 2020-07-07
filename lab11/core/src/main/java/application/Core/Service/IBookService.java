package application.Core.Service;

import application.Core.Model.Book;
import application.Core.Model.Client;
import application.Core.Model.Validators.ValidatorException;

import java.util.Set;

public interface IBookService {
    Set<Book> filterBooksByPrice(int price);
    Set<Book> filterBooksByAuthor(String author);
    Book updateBook(Long id, Book book);
    Book addBook(Book entity);
    Set<Book> getAllBooks();
    void removeBook(long id);
    Book getBookByID(long id) throws ValidatorException;
    Set<Book> sort(String direc, String args);

    void addTransaction(Client client, Book book);
}
