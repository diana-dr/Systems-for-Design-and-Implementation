package Service;

import Domain.BaseEntity;
import Domain.Validators.ValidatorException;
import Repository.Repository;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EntityService<T extends BaseEntity<Long>> {
    Repository<Long, T> repository;

    public EntityService(Repository<Long, T> repo) { repository = repo; }

    public void addEntity(T entity) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        repository.save(entity);
    }

    public Set<T> getAllEntities() {
        Iterable<T> entities = repository.findAll();
        Set<T> entitiesSet = new HashSet<>();
        entities.forEach(entitiesSet::add);
        return entitiesSet;
    }

    public void removeEntity(long id) throws ValidatorException, ParserConfigurationException, IOException, SAXException, TransformerException {
        repository.delete(id);
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
