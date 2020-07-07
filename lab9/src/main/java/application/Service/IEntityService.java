package application.Service;

import application.Domain.BaseEntity;
import application.Domain.Validators.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Set;

public interface IEntityService<T extends BaseEntity<Long>> {
    void addEntity(T entity);
    void removeEntity(long id);
    Set<T> getAllEntities();
    T getEntityByID(long id) throws ValidatorException;
    Iterable<T> sort(String direc, String ... args);
}
