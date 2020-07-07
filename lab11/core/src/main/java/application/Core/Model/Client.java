package application.Core.Model;

import lombok.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Client extends BaseEntity<Long> {

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "dateOfBirth", nullable = false)
    private String dateOfBirth;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Transaction> clientTransactions = new HashSet<>();

    public Set<Book> getBooks() {
        clientTransactions = clientTransactions == null ? new HashSet<>() :
                clientTransactions;
        return Collections.unmodifiableSet(
                this.clientTransactions.stream().
                        map(Transaction::getBook).collect(Collectors.toSet()));
    }

    public void addBook(Book book) {
        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setClient(this);
        clientTransactions.add(transaction);
    }

    public void addBooks(Set<Book> books) {
        books.forEach(this::addBook);
    }

    public void addTransaction(Book book, String date) {
        Transaction transaction = new Transaction();
        transaction.setClient(this);
        transaction.setBook(book);
        transaction.setDate(date);
        clientTransactions.add(transaction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return firstName.equals(client.firstName) &&
                lastName.equals(client.lastName) &&
                dateOfBirth.equals(client.dateOfBirth) &&
                email.equals(client.email) &&
                Objects.equals(clientTransactions, client.clientTransactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dateOfBirth, email, clientTransactions);
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", clientTransactions=" + clientTransactions +
                '}';
    }
}
