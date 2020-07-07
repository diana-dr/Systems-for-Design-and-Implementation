package socket.server.Service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import socket.common.IEntityService;
import socket.server.Domain.BaseEntity;
import socket.server.Domain.Validators.Validator;
import socket.server.Domain.Validators.ValidatorException;
import socket.server.Repository.Repository;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class EntityService<T extends BaseEntity<Long>> implements IEntityService {
    protected Repository<Long, T> repository;
    protected ExecutorService executorService;
    protected Class<T> entityClass;

    public EntityService(Repository<Long, T> repo, ExecutorService executorService) {
        repository = repo;
        this.executorService = executorService;
    }

    public Future<String> addEntity(String values) throws ValidatorException {
        return executorService.submit(() -> {
            try {
                T entity = entityClass.getDeclaredConstructor(String.class).newInstance(values);
                repository.save(entity);
                return "entity added";
            }catch (Exception e)
            {
                throw new ValidatorException(e.getMessage());
            }
        });
    }

    @Override
    public Future<String> updateEntity(String values) {
        return executorService.submit(()->{
            try {
                T entity = entityClass.getDeclaredConstructor(String.class).newInstance(values);
                repository.update(entity);
                return "updated entity";
            }catch (Exception e){
                throw new ValidatorException(e.getMessage());
            }
        });
    }

    @Override
    public Future<String> removeEntity(String id) {
        return executorService.submit(() -> {
            try{
                JSONTokener tokener = new JSONTokener(id);
                JSONObject object = new JSONObject(tokener);

                Long entity_id = object.getLong("id");
                repository.delete(entity_id);
                return "entity deleted";

            }catch (Exception e){
                throw new ValidatorException(e.getMessage());
            }
        });
    }

    public Future<String> getAllEntities() {
        return executorService.submit(()-> {
                JSONArray toReturn = new JSONArray();
                Iterable<T> entities = repository.findAll();
                entities.forEach((entity) ->
                        toReturn.put(entity.getJson()));
                return toReturn.toString();
            }
        );
    }

    public void updateEntity(T entity) throws ValidatorException, ParserConfigurationException, IOException, SAXException, TransformerException {
        repository.update(entity);
    }

    public T getEntityByID(long id) throws ValidatorException {
        if (!repository.findOne(id).isPresent())
            throw new ValidatorException("Missing entity with ID: "+id);
        return repository.findOne(id).get();
    }
}
