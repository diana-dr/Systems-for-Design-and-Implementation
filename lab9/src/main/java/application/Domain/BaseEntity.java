package application.Domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;

@MappedSuperclass
public class BaseEntity<ID extends Serializable> implements Serializable {

    @Id
    @GeneratedValue
    private ID id;

    public BaseEntity(ResultSet info) { }

    public BaseEntity() {}

    public ID getId() {

        return id;
    }

    public void setId(ID id) {

        this.id = id;
    }

    public String toString() {
        return "BaseEntity " +
                "ID: " + id + "\n\n";
    }
}
