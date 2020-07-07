package socket.server.Domain;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Transaction extends BaseEntity<Long> {
    private Long bookID;
    private Long clientID;

    public Transaction(Long bookID, Long clientID) {
        this.bookID = bookID;
        this.clientID = clientID;
    }

    public Transaction(String values) {
        JSONTokener tokener = new JSONTokener(values);
        JSONObject object = new JSONObject(tokener);

        Long ID = object.getLong("id");
        this.setId(ID);

        this.bookID = object.getLong("book_id");
        this.clientID = object.getLong("client_id");
    }

    public Transaction(ResultSet resultSet) throws SQLException {
        setId(resultSet.getLong("id"));
        this.bookID = resultSet.getLong("book_id");
        this.clientID = resultSet.getLong("client_id");
    }

    public Transaction() {}

    public Long getBookID() {
        return bookID;
    }

    public Long getClientID() {
        return clientID;
    }

    public Long getId() {
        return super.getId();
    }

    public Transaction getEntity() {return this;}

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookID + "\n" +
                "Client ID: " + clientID + "\n" +
                super.toString() + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(),bookID, clientID);
    }

    @Override
    public JSONObject getJson() {
        JSONObject object = new JSONObject();
        object.put("book_id", this.bookID);
        object.put("client_id", this.clientID);
        object.put("id", this.getId());

        return object;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null || getClass() != object.getClass())
            return false;

        Transaction transaction = (Transaction) object;

        return Objects.equals(bookID, transaction.bookID) &&
                Objects.equals(clientID, transaction.clientID);
    }
}
