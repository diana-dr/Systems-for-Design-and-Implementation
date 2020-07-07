package application.Core.Service;

import application.Core.Domain.BaseEntity;
import application.Core.Domain.Validators.Validator;
import application.Core.Domain.Validators.ValidatorException;
import application.Core.Repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import java.util.*;

public class EntityServiceImpl<T extends BaseEntity<Long>> implements IEntityService<T> {

    public static final Logger log = LoggerFactory.getLogger(EntityServiceImpl.class);
    Validator<T> validator;

    Repository<Long, T> repository;

    public EntityServiceImpl(Repository<Long, T> repo) { repository = repo; }

    @Override
    public T addEntity(T entity) {
        log.trace("addEntity - method entered: entity={}", entity);
        validator.validate(entity);
        repository.save(entity);
        log.trace("addEntity - method finished");
        return entity;
    }

    @Override
    public Set<T> getAllEntities() {
        log.trace("getAllEntities --- method entered");

        Iterable<T> entities = repository.findAll();
        Set<T> entitiesSet = new HashSet<>();
        entities.forEach(entitiesSet::add);

        log.trace("getAllEntities: result={}", entitiesSet);
        return entitiesSet;
    }

    @Override
    public void removeEntity(long id) {
        T entity = getEntityByID(id);
        log.trace("deleteEntity() --- method entered: entity={}", entity);
        repository.deleteById(id);
        log.trace("deleteEntity() --- deleted: entity={}", entity);
    }

    @Override
    public T getEntityByID(long id) throws ValidatorException {
        log.trace("getEntityByID() --- method entered: entityID={}", id);
        if (repository.findById(id).isEmpty())
            throw new ValidatorException("Missing entity with ID: " + id);
        T entity = repository.findById(id).get();
        log.trace("getEntityByID() --- method finished: entity={}", entity);
        return entity;
    }

    @Override
    public Set<T> sort(String direc, String args) {
        log.trace("sort() --- method entered: direction={}, args={}", direc, args);
        Set<T> result = new HashSet<>();
        if (direc.equals("ASC")) {
            Iterable<T> entities = repository.findAll(Sort.by(Sort.Direction.ASC, args));
            entities.forEach(result::add);
        } else if (direc.equals("DESC")) {
            Iterable<T> entities = repository.findAll(Sort.by(Sort.Direction.DESC, args));
            entities.forEach(result::add); }

        log.trace("sort() --- method finished: result={}", result);
        return result;
    }
}
