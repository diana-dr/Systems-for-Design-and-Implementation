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
public class Book extends BaseEntity<Long> {

    @Column(name="category", nullable = false)
    private String category;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="author", nullable = false)
    private String author;

    @Column(name="releaseDate", nullable = false)
    private String releaseDate;

    @Column(name="price", nullable = false)
    private int price;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Transaction> bookTransactions = new HashSet<>();

    public Set<Client> getClients() {
        return Collections.unmodifiableSet(
                bookTransactions.stream()
                        .map(Transaction::getClient)
                        .collect(Collectors.toSet())
        );
    }

    public void addClient(Client client) {
        Transaction transaction = new Transaction();
        transaction.setClient(client);
        transaction.setBook(this);
        bookTransactions.add(transaction);
    }

    public void addTransaction(Client client, String date) {
        Transaction transaction = new Transaction();
        transaction.setClient(client);
        transaction.setBook(this);
        transaction.setDate(date);
        bookTransactions.add(transaction);
    }

    public void addClients(Set<Client> clients) {
        clients.forEach(this::addClient);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return price == book.price &&
                category.equals(book.category) &&
                title.equals(book.title) &&
                author.equals(book.author) &&
                releaseDate.equals(book.releaseDate) &&
                Objects.equals(bookTransactions, book.bookTransactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, title, author, releaseDate, price, bookTransactions);
    }

    @Override
    public String toString() {
        return "Book{" +
                "category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", price=" + price +
                ", bookTransactions=" + bookTransactions +
                '}';
    }
}
