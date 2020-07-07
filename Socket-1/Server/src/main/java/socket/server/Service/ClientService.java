package socket.server.Service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import socket.common.IClientService;
import socket.server.Domain.Book;
import socket.server.Domain.Client;
import socket.server.Repository.JDBC.Direction;
import socket.server.Repository.JDBC.Sort;
import socket.server.Repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ClientService extends EntityService<Client> implements IClientService {

    public ClientService(Repository<Long, Client> repo, ExecutorService executorService) {
        super(repo, executorService);
        entityClass = Client.class;
    }

    public Iterable<Client> sort(String direc, String ... args){
        List<Client> list = new ArrayList<>();
        if (direc.equals("ASC")){
            Sort<Client> sort = new Sort<>(Direction.ASC,args);
            return repository.findAll(sort);
        }
        else {
            Sort<Client> sort = new Sort<>(Direction.DESC,args);
            return repository.findAll(sort);
        }
    }

    public Future<String> printSortedClients(String values) {
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
            Iterable<Client> entities = this.sort(direction, args);
            entities.forEach((entity) ->
                    toReturn.put(entity.getJson()));
            return toReturn.toString();
        });
    }
}
