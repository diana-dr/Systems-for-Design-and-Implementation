package Domain;

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

    public Transaction(ResultSet resultSet) throws SQLException {
        setId(resultSet.getLong("id"));
        this.bookID = resultSet.getLong("book_id");
        this.clientID = resultSet.getLong("client_id");
    }

    public Transaction(){}

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
