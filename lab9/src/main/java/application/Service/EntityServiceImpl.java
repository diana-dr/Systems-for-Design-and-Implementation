package application.Service;

import application.Domain.BaseEntity;
import application.Domain.Validators.ValidatorException;
import application.Repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class EntityServiceImpl<T extends BaseEntity<Long>> implements IEntityService<T> {

    public static final Logger log = LoggerFactory.getLogger(EntityServiceImpl.class);

    Repository<Long, T> repository;

    public EntityServiceImpl(Repository<Long, T> repo) { repository = repo; }

    @Override
    public void addEntity(T entity) {
        log.trace("addEntity - method entered: entity={}", entity);
        repository.save(entity);
        log.debug("addEntity - added: entity={}", entity);
        log.trace("addEntity - method finished");
    }

    @Override
    public Set<T> getAllEntities() {
        Iterable<T> entities = repository.findAll();
        Set<T> entitiesSet = new HashSet<>();
        entities.forEach(entitiesSet::add);
        return entitiesSet;
    }

    @Override
    public void removeEntity(long id) {
        T entity = getEntityByID(id);
        log.trace("deleteEntity - method entered: entity={}", entity);
        repository.deleteById(id);
        log.debug("deleteEntity - deleted: entity={}", entity);
        log.trace("deleteEntity - method finished");
    }

    @Override
    public T getEntityByID(long id) throws ValidatorException {
        if (repository.findById(id).isEmpty())
            throw new ValidatorException("Missing entity with ID: " + id);
        return repository.findById(id).get();
    }

    @Override
    public Iterable<T> sort(String direc, String ... args) {
        if (direc.equals("ASC"))
            return repository.findAll(Sort.by(Sort.Direction.ASC, args));
        else
            return repository.findAll(Sort.by(Sort.Direction.DESC, args));
    }
}
