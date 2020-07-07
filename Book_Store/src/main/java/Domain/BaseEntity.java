package Domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BaseEntity<ID> {

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
