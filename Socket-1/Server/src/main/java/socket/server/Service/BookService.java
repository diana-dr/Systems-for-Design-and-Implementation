package socket.server.Service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import socket.common.IBookService;
import socket.server.Domain.Book;
import socket.server.Repository.JDBC.Direction;
import socket.server.Repository.JDBC.Sort;
import socket.server.Repository.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class BookService  extends EntityService<Book> implements IBookService {

    public BookService(Repository<Long, Book> repo, ExecutorService executorService) {
        super(repo, executorService);
        entityClass = Book.class;
    }

    public Future<String> filterBooksByPrice(String value) {
        return executorService.submit(()-> {
                    JSONArray toReturn = new JSONArray();
                    List<Book> filteredBooks = new ArrayList<>();
                    Iterable<Book> books = repository.findAll();

                    JSONTokener tokener = new JSONTokener(value);
                    JSONObject object = new JSONObject(tokener);
                    int price = object.getInt("price");

                    books.forEach(filteredBooks::add);
                    System.out.println(filteredBooks);

                    filteredBooks.stream().filter(book -> book.getPrice() == price).forEach((entity) ->
                            toReturn.put(entity.getJson()));
                    return toReturn.toString();
                }
        );
    }

    public Future<String> filterBooksByAuthor(String value) {
        return executorService.submit(()-> {
                    JSONArray toReturn = new JSONArray();
                    List<Book> filteredBooks = new ArrayList<>();

                    Iterable<Book> books = repository.findAll();

                    JSONTokener tokener = new JSONTokener(value);
                    JSONObject object = new JSONObject(tokener);
                    String author = object.getString("author");
                    books.forEach(filteredBooks::add);

                    filteredBooks.stream().filter(book -> book.getAuthor().equals(author)).forEach((entity) ->
                            toReturn.put(entity.getJson()));
                    return toReturn.toString();
                }
        );
    }

    public Iterable<Book> sort(String direc, String ... args){
        List<Book> list = new ArrayList<>();
        if (direc.equals("ASC")){
            Sort<Book> sort = new Sort<>(Direction.ASC,args);
            return repository.findAll(sort);
        }
        else {
            Sort<Book> sort = new Sort<>(Direction.DESC,args);
            return repository.findAll(sort);
        }
    }

    @Override
    public Future<String> printSortedBooks(String values) {
        return executorService.submit(() -> {
            JSONTokener tokener = new JSONTokener(values);
            JSONObject object = new JSONObject(tokener);
            int length_of_args = object.getInt("number_of_args");
            String[] args = new String[length_of_args];
            for (int i = 0; i < length_of_args; i++) {
                args[i] = object.getString("arg" + i);
            }

            String direction = object.getString("direction");

            JSONArray toReturn = new JSONArray();
            Iterable<Book> entities = this.sort(direction, args);
            entities.forEach((entity) ->
                    toReturn.put(entity.getJson()));
            return toReturn.toString();
        });
    }
}

