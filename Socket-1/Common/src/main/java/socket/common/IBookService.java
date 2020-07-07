package socket.common;

import java.util.concurrent.Future;

public interface IBookService extends IEntityService {

    String FILTER_BOOKS_BY_PRICE = "filterBooksByPrice";
    String FILTER_BOOKS_BY_AUTHOR = "filterBooksByAuthor";
    Future<String> filterBooksByPrice(String price);
    Future<String> filterBooksByAuthor(String toString);
    Future<String> printSortedBooks(String entity);
}
