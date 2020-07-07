package socket.server.Domain;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.ResultSet;

public class BaseEntity<ID> {

    private ID id;

    public BaseEntity(ResultSet info) { }

    public BaseEntity(String values) { }

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

    public JSONObject getJson() { return null; }
}
