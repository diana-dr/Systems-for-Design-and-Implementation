package socket.common;

import org.json.JSONArray;
import java.util.concurrent.Future;

public interface IEntityService {
    String ADD_ENTITY = "addEntity";
    String GET_ENTITIES = "getAllEntities";
    String REMOVE_ENTITY = "removeEntity";
    String UPDATE_ENTITY = "updateEntity";
    String PRINT_SORTED_BOOKS = "printSortedBooks";
    String PRINT_SORTED_CLIENTS = "printSortedClients";
    Future<String> addEntity(String entity);
    Future<String> getAllEntities();
    Future<String> removeEntity(String id);
    Future<String> updateEntity(String entity);
}
