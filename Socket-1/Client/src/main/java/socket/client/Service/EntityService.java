package socket.client.Service;

import org.json.JSONArray;
import socket.client.TCP.TcpClient;
import socket.common.IEntityService;
import socket.common.Request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class EntityService implements IEntityService {
    protected ExecutorService executorService;
    protected TcpClient tcpClient;

    public EntityService(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<String> addEntity(String entity) {
        return executorService.submit(() -> {
            Request request = new Request(EntityService.ADD_ENTITY, entity);
            System.out.println("sending request: "+ request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }

    @Override
    public Future<String> getAllEntities() {
        return executorService.submit(() -> {
            Request request = new Request(EntityService.GET_ENTITIES, this.getClass().toString());
            System.out.println("sending request: "+request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }

    @Override
    public Future<String> removeEntity(String id) {
        return executorService.submit(()-> {
            Request request = new Request(EntityService.REMOVE_ENTITY, id);
            System.out.println("sending request: "+ request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);

            return response.getBody();
        });
    }

    @Override
    public Future<String> updateEntity(String entity) {
        return executorService.submit(()->{
            Request request = new Request(EntityService.UPDATE_ENTITY, entity);
            System.out.println("sending request: " + request);
            Request response = tcpClient.sendAndReceive(request);
            System.out.println("Received response: " + response);

            return response.getBody();
        });
    }
}
