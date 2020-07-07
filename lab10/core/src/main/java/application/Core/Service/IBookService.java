package application.Core.Service;

import application.Core.Domain.Book;

import java.util.Set;

public interface IBookService extends IEntityService<Book> {
    Set<Book> filterBooksByPrice(int price);
    Set<Book> filterBooksByAuthor(String author);
    Book updateBook(Long id, Book book);
}