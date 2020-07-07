package application.Core.Service;

import application.Core.Domain.BaseEntity;
import application.Core.Domain.Validators.ValidatorException;

import java.util.List;
import java.util.Set;

public interface IEntityService<T extends BaseEntity<Long>> {
    T addEntity(T entity);
    void removeEntity(long id);
    Set<T> getAllEntities();
    T getEntityByID(long id) throws ValidatorException;
    Set<T> sort(String direc, String args);
}
