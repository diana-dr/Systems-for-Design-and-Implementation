package application.Client.UI;

import application.Core.Domain.Book;
import application.Core.Domain.Client;
import application.Core.Domain.Transaction;
import application.Core.Domain.Validators.ValidatorException;
import application.Web.Dto.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

@Component
public class Console {

    private final RestTemplate restTemplate;
    public static final String bookURL = "http://localhost:8080/api/books";
    public static final String clientURL = "http://localhost:8080/api/clients";
    public static final String transactionURL = "http://localhost:8080/api/transactions";

    Map<Integer, Consumer<BufferedReader>> commands;

    public Console(AnnotationConfigApplicationContext context) {
        restTemplate = context.getBean(RestTemplate.class);

        commands = new HashMap<>();
        commands.put(0, this::exit);
        commands.put(1, this::printAllBooks);
        commands.put(2, this::printAllClients);
        commands.put(3, this::addBook);
        commands.put(4, this::addClient);
        commands.put(5, this::deleteBook);
        commands.put(6, this::deleteClient);
        commands.put(7, this::updateBook);
        commands.put(8, this::updateClient);
        commands.put(9, this::buyBook);
        commands.put(10, this::printTransactions);
        commands.put(11, this::filterBooksByPrice);
        commands.put(12, this::filterBooksByAuthor);
        commands.put(13, this::printSortBy);
    }

    public void printSortBy(BufferedReader bufferedReader) {
        try {
            System.out.println("For what class? book/client");
            String className = bufferedReader.readLine();

            if (className.contains("book")){
                printSortedBooks(bufferedReader);
            }
            else if (className.contains("client")) {
                printSortedClients(bufferedReader);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void filterBooksByAuthor(BufferedReader bufferedReader) {
        try {
            System.out.println("Book Author: ");
            String author = bufferedReader.readLine();
            BooksDto books = restTemplate.getForObject(bookURL + "/{author}", BooksDto.class, author);
            System.out.println(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void filterBooksByPrice(BufferedReader bufferedReader) {
        try {
            System.out.println("Book Price: ");
            int price = Integer.parseInt(bufferedReader.readLine());

            BooksDto books = restTemplate.getForObject(bookURL + "/filtered" +"/{price}", BooksDto.class, price);
            System.out.println(books);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printSortedBooks(BufferedReader bufferedReader){
        try {
            System.out.println("For what fields? ");
            String args = bufferedReader.readLine();
            System.out.println("ASC or DESC?");
            String direction = bufferedReader.readLine();
            BooksDto books = restTemplate.getForObject(bookURL + "/sorted" + "/{direction}" + "/{args}",
                    BooksDto.class, direction, args);
            System.out.println(books);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void printSortedClients(BufferedReader bufferedReader) {
        try {
            System.out.println("For what fields? ");
            String args = bufferedReader.readLine();
            System.out.println("ASC or DESC?");
            String direction = bufferedReader.readLine();
            ClientsDto clients = restTemplate.getForObject(clientURL + "/sorted" + "/{direction}" + "/{args}",
                    ClientsDto.class, direction, args);
            System.out.println(clients);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void exit(BufferedReader bufferedReader) {
        System.exit(0);
    }

    public void updateClient(BufferedReader bufferedReader) {
        try {
            System.out.println("ID: ");
            long id = Long.parseLong(bufferedReader.readLine());

            System.out.println("First Name: ");
            String firstName = bufferedReader.readLine();

            System.out.println("Last Name: ");
            String lastName = bufferedReader.readLine();

            System.out.println("Date Of Birth: ");
            String dateOfBirth = bufferedReader.readLine();

            System.out.println("Email: ");
            String email = bufferedReader.readLine();

            ClientDto client = restTemplate.getForObject(clientURL, ClientDto.class, id);
            if (client != null) {
                client.setFirstName(firstName);
                client.setLastName(lastName);
                client.setDateOfBirth(dateOfBirth);
                client.setEmail(email);
            }

            restTemplate.put(clientURL + "/{id}", client, id);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateBook(BufferedReader bufferedReader) {
        try {
            System.out.println("ID: ");
            long id = Long.parseLong(bufferedReader.readLine());

            System.out.println("Category: ");
            String category = bufferedReader.readLine();

            System.out.println("Title: ");
            String title = bufferedReader.readLine();

            System.out.println("Author: ");
            String author = bufferedReader.readLine();

            System.out.println("Release Date: ");
            String date = bufferedReader.readLine();

            System.out.println("Price: ");
            int price = Integer.parseInt(bufferedReader.readLine());

            BookDto book = restTemplate.getForObject(bookURL, BookDto.class, id);
            if (book != null) {
                book.setCategory(category);
                book.setTitle(title);
                book.setAuthor(author);
                book.setPrice(price);
                book.setReleaseDate(date);
                book.setPrice(price);
            }

            restTemplate.put(bookURL + "/{id}", book, id);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteBook(BufferedReader bufferedReader) {
        try {
            System.out.println("Book ID: ");
            long id = Long.parseLong(bufferedReader.readLine());

            restTemplate.delete(bookURL + "/{id}", id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteClient(BufferedReader bufferedReader) {
        try {
            System.out.println("Client ID: ");
            long id = Long.parseLong(bufferedReader.readLine());

            restTemplate.delete(clientURL + "/{id}", id);

        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    public void buyBook(BufferedReader bufferedReader) {
        try {

            System.out.println("Book ID: ");
            long bookID = Long.parseLong(bufferedReader.readLine());

            System.out.println("Client ID: ");
            long clientID = Long.parseLong(bufferedReader.readLine());

            Transaction newTransaction = new Transaction(bookID, clientID);
            TransactionDto savedTransaction = restTemplate.postForObject(transactionURL, newTransaction, TransactionDto.class);
            System.out.println("savedTransaction: " + savedTransaction);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    public void addBook(BufferedReader bufferedReader){
        try {
            Book book = readBooks(bufferedReader);
            BookDto savedBook = restTemplate.postForObject(bookURL, book, BookDto.class);
            System.out.println("savedBook: " + savedBook);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    public void addClient(BufferedReader bufferedReader){
        try {
            Client client = readClient(bufferedReader);
            ClientDto savedClient = restTemplate.postForObject(clientURL, client, ClientDto.class);
            System.out.println("savedClient: " + savedClient);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    Book readBooks(BufferedReader bufferedReader) {
        try {

            System.out.println("Category: ");
            String category = bufferedReader.readLine();

            System.out.println("Title: ");
            String title = bufferedReader.readLine();

            System.out.println("Author: ");
            String author = bufferedReader.readLine();

            System.out.println("Release Date: ");
            String date = bufferedReader.readLine();

            System.out.println("Price: ");
            int price = Integer.parseInt(bufferedReader.readLine());

            return new Book(category, title, author, date, price);
        } catch (ValidatorException | IOException v) {
            v.printStackTrace();
        }
        return null;
    }

    Client readClient(BufferedReader bufferedReader) {
        try {
            System.out.println("First Name: ");
            String firstName = bufferedReader.readLine();

            System.out.println("Last Name: ");
            String lastName = bufferedReader.readLine();

            System.out.println("Date Of Birth: ");
            String dateOfBirth = bufferedReader.readLine();

            System.out.println("Email: ");
            String email = bufferedReader.readLine();

            return new Client(firstName, lastName, dateOfBirth, email);
        } catch (ValidatorException | IOException v) {
            v.printStackTrace();
        }
        return null;
    }

    void printMenu() {

        System.out.println("\t 0. EXIT ");
        System.out.println("\t 1. Print all books ");
        System.out.println("\t 2. Print all clients ");
        System.out.println("\t 3. Add a book ");
        System.out.println("\t 4. Add client ");
        System.out.println("\t 5. Delete book ");
        System.out.println("\t 6. Delete client");
        System.out.println("\t 7. Update book ");
        System.out.println("\t 8. Update client ");
        System.out.println("\t 9. Buy book ");
        System.out.println("\t 10. Print all transactions ");
        System.out.println("\t 11. Filter books by price");
        System.out.println("\t 12. Filter books by author");
        System.out.println("\t 13. Sort by");
        System.out.println("Choose an option: ");
    }

    void printAllBooks(BufferedReader bufferedReader) {
        BooksDto books = restTemplate.getForObject(bookURL, BooksDto.class);
        System.out.println(books);
    }

    void printAllClients(BufferedReader bufferedReader) {
        ClientsDto clients = restTemplate.getForObject(clientURL, ClientsDto.class);
        System.out.println(clients);
    }

    public void printTransactions(BufferedReader bufferedReader) {
        TransactionsDto transactions = restTemplate.getForObject(transactionURL, TransactionsDto.class);
        System.out.println(transactions);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void runConsole() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                printMenu();
                int input = Integer.parseInt(bufferedReader.readLine());
                Consumer<BufferedReader> command = commands.get(input);
                if (command != null)
                    command.accept(bufferedReader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
