package UI;

import Domain.Book;
import Domain.Client;
import Domain.Transaction;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Service.BookService;
import Service.ClientService;
import Service.TransactionService;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

public class Console {

    BookService bookService;
    ClientService clientService;
    Map<Integer, Consumer<BufferedReader>> commands;
    TransactionService transactionService;

    public Console(BookService bookService, ClientService clientService, TransactionService transactionService) throws IOException, TransformerException{
        this.bookService = bookService;
        this.clientService = clientService;
        this.transactionService = transactionService;
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
        commands.put(13, this::sortClients);
        commands.put(14, this::sortBooks);
        commands.put(15, this::printSortBy);
    }

    public void printSortBy(BufferedReader bufferedReader){
        try {
            System.out.println("For what class?");
            String className = bufferedReader.readLine();

            if (className.equals("Books")){
                printSortedBooks(bufferedReader);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void printTransactions(BufferedReader bufferedReader) {
        Set<Transaction> transactions = transactionService.getAllEntities();
        System.out.println("Transactions\n");
        transactions.forEach(System.out::println);
    }

    public void sortClients(BufferedReader bufferedReader) {
        NavigableMap<Integer, Client> treeMap = transactionService.sortClients().descendingMap();
        if (treeMap.size() > 1)
            treeMap.forEach((key, value) ->  System.out.println("Total money spent: " + key + "\n" + value));
        else
            System.out.println("No one has bought any books yet!");
    }

    public void sortBooks(BufferedReader bufferedReader) {
        NavigableMap<Integer, Book> treeMap = transactionService.sortBooks().descendingMap();
        if (treeMap.size() > 1)
            treeMap.forEach((key, value) ->  System.out.println("Times book was bought: " + key + "\n" + value));
        else
            System.out.println("No one has bought any books yet!");
    }

    public void filterBooksByAuthor(BufferedReader bufferedReader) {
        try {
            System.out.println("Book Author: ");
            String author = bufferedReader.readLine();

            Set<Book> filteredBooks  = this.bookService.filterBooksByAuthor(author);
            filteredBooks.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printSortedBooks(BufferedReader bufferedReader){
        String[] args;
        try {
            System.out.println("For what fields? ");
            args = bufferedReader.readLine().split(",");
            System.out.println("ASC or DESC?");
            String direction = bufferedReader.readLine();
            for (Book book : bookService.sort(direction, args)) {
                System.out.println(book);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void filterBooksByPrice(BufferedReader bufferedReader) {
        try {
            System.out.println("Book Price: ");
            int bookPrice = Integer.parseInt(bufferedReader.readLine());

            Set<Book> filteredBooks  = this.bookService.filterBooksByPrice(bookPrice);
            filteredBooks.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(BufferedReader bufferedReader) {
        System.exit(0);
    }

    public void updateBook(BufferedReader bufferedReader) {
        try{
            Book book = readBooks(bufferedReader);
            this.bookService.updateEntity(book);
        }catch (IOException | ParserConfigurationException | SAXException | TransformerException e){
            e.printStackTrace();
        }

    }

    public void updateClient(BufferedReader bufferedReader) {
        try{Client clients = readClient(bufferedReader);
            this.clientService.updateEntity(clients);
        }catch (IOException | ParserConfigurationException | SAXException | TransformerException e){
            e.printStackTrace();
        }

    }


    public void deleteBook(BufferedReader bufferedReader) {
        try {
            System.out.println("Book ID: ");
            long bookID = Long.parseLong(bufferedReader.readLine());

            this.transactionService.removeRelationBook(bookID);
            this.bookService.removeEntity(bookID);

        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }


    public void deleteClient(BufferedReader bufferedReader) {
        try {
            System.out.println("Client ID: ");
            long clientID = Long.parseLong(bufferedReader.readLine());

            this.transactionService.removeRelationClient(clientID);
            this.clientService.removeEntity(clientID);

        } catch (ValidatorException | IOException | ParserConfigurationException | SAXException | TransformerException v) {
            v.printStackTrace();
        }
    }

    public void buyBook(BufferedReader bufferedReader) {
        try {
//            System.out.println("Transaction ID: ");
//            long transactionID = Long.parseLong(bufferedReader.readLine());

            System.out.println("Book ID: ");
            long bookID = Long.parseLong(bufferedReader.readLine());

            System.out.println("Client ID: ");
            long clientID = Long.parseLong(bufferedReader.readLine());

            Transaction newTransaction = new Transaction(bookID, clientID);
//            newTransaction.setId(transactionID);

            transactionService.addEntity(newTransaction);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    public void addBook(BufferedReader bufferedReader){
        try {
            Book book = readBooks(bufferedReader);
            bookService.addEntity(book);
        } catch (ValidatorException | TransformerException | SAXException | ParserConfigurationException | IOException v) {
            v.printStackTrace();
        }
    }

    public void addClient(BufferedReader bufferedReader){
        try {
            Client client = readClient(bufferedReader);
            clientService.addEntity(client);
        } catch (ValidatorException | IOException | ParserConfigurationException | SAXException | TransformerException v) {
            v.printStackTrace();
        }
    }

    Book readBooks(BufferedReader bufferedReader) {
        try {
//            System.out.println("ID: ");
//            Long ID = Long.valueOf(bufferedReader.readLine());

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

            // book.setId(ID);

            return new Book(category, title, author, date, price);
        } catch (ValidatorException | IOException v) {
            v.printStackTrace();
        }
        return null;
    }

    Client readClient(BufferedReader bufferedReader) {
        try {
//            System.out.println("ID: ");
//            Long ID = Long.valueOf(bufferedReader.readLine());

            System.out.println("First Name: ");
            String firstName = bufferedReader.readLine();

            System.out.println("Last Name: ");
            String lastName = bufferedReader.readLine();

            System.out.println("Date Of Birth: ");
            String dateOfBirth = bufferedReader.readLine();

            System.out.println("Email: ");
            String email = bufferedReader.readLine();

            //            client.setId(ID);

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
        System.out.println("\t 13. Sort clients based on the spent amount of money");
        System.out.println("\t 14. Sort books based on the times it was bought");
        System.out.println("\t 15. Sort by:");
        System.out.println("Choose an option: ");
    }

    void printAllBooks(BufferedReader bufferedReader) {
        Set<Book> books = bookService.getAllEntities();
        System.out.println("Books\n");
        books.forEach(System.out::println);
    }

    void printAllClients(BufferedReader bufferedReader) {
        Set<Client> clients = clientService.getAllEntities();
        System.out.println("Clients\n");
        clients.forEach(System.out::println);
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
