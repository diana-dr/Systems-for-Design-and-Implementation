package socket.common;

import sun.net.www.protocol.file.FileURLConnection;

import java.util.concurrent.Future;

public interface ITransactionService extends IEntityService {

    String SORT_CLIENTS = "sortClients";
    String SORT_BOOKS = "sortBooks";
    String REMOVE_RELATION_CLIENT = "removeRelationClient";
    String REMOVE_RELATION_BOOK = "removeRelationBook";

    Future<String> sortClients();
    Future<String> sortBooks();
    Future<String> removeRelationClient(String toString);
    Future<String> removeRelationBook(String toString);
}
