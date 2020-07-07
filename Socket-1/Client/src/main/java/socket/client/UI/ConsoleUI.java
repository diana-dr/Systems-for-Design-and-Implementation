package socket.client.UI;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import socket.client.Service.BookService;
import socket.client.Service.ClientService;
import socket.client.Service.TransactionService;
import org.xml.sax.SAXException;
import socket.client.Service.BookService;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

public class ConsoleUI {

    BookService bookService;
    ClientService clientService;
    Map<Integer, Consumer<BufferedReader>> commands;
    TransactionService transactionService;

    public ConsoleUI(BookService bookService, ClientService clientService, TransactionService transactionService) throws IOException, TransformerException{
        this.bookService = bookService;
        this.clientService = clientService;
        this.transactionService = transactionService;
        commands = new HashMap<>();
        commands.put(0, this::exit);
        commands.put(1, this::printAllBooks); //implemented
        commands.put(2, this::printAllClients); //implemented
        commands.put(3, this::addBook);  // implemented
        commands.put(4, this::addClient); //implemented
        commands.put(5, this::deleteBook); //implemented
        commands.put(6, this::deleteClient); //implemented
        commands.put(7, this::updateBook); // implemented
        commands.put(8, this::updateClient); //implemented
        commands.put(9, this::buyBook); //implemented
        commands.put(10, this::printTransactions); // implemented
        commands.put(11, this::filterBooksByPrice); // implemented
        commands.put(12, this::filterBooksByAuthor); // implemented
        commands.put(13, this::sortClients);
        commands.put(14, this::sortBooks);
        commands.put(15, this::printSortBy); //implemented
    }


    public void printTransactions(BufferedReader bufferedReader) {
        try {
            if (transactionService.getAllEntities().isCancelled())
                System.out.println("Operation timed out");

            JSONTokener tokener = new JSONTokener(transactionService.getAllEntities().get());
            JSONArray array = new JSONArray(tokener);
            StringBuilder sb = new StringBuilder();

            for (Object objectAbs : array) {
                JSONObject object = (JSONObject) objectAbs;
                sb.append("Client ID: ").append(object.getLong("client_id"));
                sb.append(" Book ID: ").append(object.getLong("book_id"));
                sb.append(" ID: ").append(object.getLong("id"));
                sb.append("\n");
            };
            System.out.println(sb.toString());

        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void sortClients(BufferedReader bufferedReader) {
        try {

            if (transactionService.sortClients().isCancelled())
                System.out.println("Operation timed out");

            JSONTokener tokener = new JSONTokener(transactionService.sortClients().get());
            JSONArray array = new JSONArray(tokener);
            StringBuilder sb = new StringBuilder();

            for (Object objectAbs : array) {

                JSONObject object = (JSONObject) objectAbs;
                sb.append("Total money spent: ").append(object.getInt("total"));
                sb.append("\nID: ").append(object.getLong("id"));
                sb.append(" First name: ").append(object.getString("firstName"));
                sb.append(" LastName: ").append(object.getString("lastName"));
                sb.append(" Date of birth: ").append(object.getString("dateOfBirth"));
                sb.append(" Email: ").append(object.getString("email"));
                sb.append("\n");

            };
            System.out.println(sb.toString());


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sortBooks(BufferedReader bufferedReader) {
        try {

            if (transactionService.sortBooks().isCancelled())
                System.out.println("Operation timed out");

            JSONTokener tokener = new JSONTokener(transactionService.sortBooks().get());
            JSONArray array = new JSONArray(tokener);
            StringBuilder sb = new StringBuilder();

            for (Object objectAbs : array) {

                JSONObject object = (JSONObject) objectAbs;
                sb.append("Times book was bought: ").append(object.getInt("count"));
                sb.append("\nID: ").append(object.getLong("id"));
                sb.append(" Title: ").append(object.getString("title"));
                sb.append(" Author: ").append(object.getString("author"));
                sb.append(" Release Date: ").append(object.getString("date"));
                sb.append(" Price: ").append(object.getInt("price"));
                sb.append("\n");


            };
            System.out.println(sb.toString());


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void filterBooksByAuthor(BufferedReader bufferedReader) {
        try {

            System.out.println("Book Author: ");
            String author = bufferedReader.readLine();
            JSONObject obj = new JSONObject();

            obj.put("author", author);

            if (bookService.filterBooksByAuthor(obj.toString()).isCancelled())
                System.out.println("Operation timed out");

            JSONTokener tokener = new JSONTokener(bookService.filterBooksByAuthor(obj.toString()).get());
            JSONArray array = new JSONArray(tokener);
            StringBuilder sb = new StringBuilder();

            for (Object objectAbs : array) {

                JSONObject object = (JSONObject) objectAbs;
                sb.append("ID: ").append(object.getLong("id"));
                sb.append(" Title: ").append(object.getString("title"));
                sb.append(" Author: ").append(object.getString("author"));
                sb.append(" Release Date: ").append(object.getString("date"));
                sb.append(" Price: ").append(object.getInt("price"));
                sb.append("\n");

            };
            System.out.println(sb.toString());


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printSortedBooks(BufferedReader bufferedReader){
        JSONObject object = new JSONObject();
        String[] args;
        try {
            System.out.println("For what fields? ");
            args = bufferedReader.readLine().split(",");
            int length = args.length;
            object.put("number_of_args", length);
            for (int i=0; i<length; i++){
                object.put("arg" + i, args[i]);
            }
            System.out.println("ASC or DESC?");
            String direction = bufferedReader.readLine();
            object.put("direction", direction);

            JSONTokener tokener = new JSONTokener(bookService.printSortedBooks(object.toString()).get());
            JSONArray array = new JSONArray(tokener);
            StringBuilder sb = new StringBuilder();
            for (Object objectAbs : array) {
                object = (JSONObject) objectAbs;
                sb.append("ID: ").append(object.getLong("id"));
                sb.append(" Title: ").append(object.getString("title"));
                sb.append(" Author: ").append(object.getString("author"));
                sb.append(" Release Date: ").append(object.getString("date"));
                sb.append(" Price: ").append(object.getInt("price"));
                sb.append("\n");
            };
            System.out.println(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void printSortedClients(BufferedReader bufferedReader){
        JSONObject object = new JSONObject();
        String[] args;
        try {
            System.out.println("For what fields? ");
            args = bufferedReader.readLine().split(",");
            int length = args.length;
            object.put("number_of_args", length);
            for (int i=0; i<length; i++){
                object.put("arg" + i, args[i]);
            }
            System.out.println("ASC or DESC?");
            String direction = bufferedReader.readLine();
            object.put("direction", direction);

            JSONTokener tokener = new JSONTokener(clientService.printSortedClients(object.toString()).get());
            JSONArray array = new JSONArray(tokener);
            StringBuilder sb = new StringBuilder();
            for (Object objectAbs : array) {
                object = (JSONObject) objectAbs;
                sb.append("Date of birth: ").append(object.getString("dateOfBirth"));
                sb.append(" Email: ").append(object.getString("email"));
                sb.append(" First name: ").append(object.getString("firstName"));
                sb.append(" LastName: ").append(object.getString("lastName"));
                sb.append(" ID: ").append(object.getLong("id"));
                sb.append("\n");
            };
            System.out.println(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void filterBooksByPrice(BufferedReader bufferedReader) {
        try {
            System.out.println("Book Price: ");
            int price = Integer.parseInt(bufferedReader.readLine());
            JSONObject obj = new JSONObject();

            obj.put("price", price);

            if (bookService.filterBooksByPrice(obj.toString()).isCancelled())
                System.out.println("Operation timed out");

            JSONTokener tokener = new JSONTokener(bookService.filterBooksByPrice(obj.toString()).get());
            JSONArray array = new JSONArray(tokener);
            StringBuilder sb = new StringBuilder();

            for (Object objectAbs : array) {

                JSONObject object = (JSONObject) objectAbs;
                sb.append("ID: ").append(object.getLong("id"));
                sb.append(" Title: ").append(object.getString("title"));
                sb.append(" Author: ").append(object.getString("author"));
                sb.append(" Release Date: ").append(object.getString("date"));
                sb.append(" Price: ").append(object.getInt("price"));
                sb.append("\n");

            };
            System.out.println(sb.toString());


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void exit(BufferedReader bufferedReader) {
        System.exit(0);
    }

    public void updateClient(BufferedReader bufferedReader) {
        try {
            System.out.println("ID: ");
            Long ID = Long.valueOf(bufferedReader.readLine());

            System.out.println(" First Name: ");
            String firstName = bufferedReader.readLine();

            System.out.println(" Last Name: ");
            String lastName = bufferedReader.readLine();

            System.out.println(" Date Of Birth: ");
            String dateOfBirth = bufferedReader.readLine();

            System.out.println(" Email: ");
            String email = bufferedReader.readLine();

            JSONObject obj = new JSONObject();

            obj.put("id", ID);
            obj.put("firstName", firstName);
            obj.put("lastName", lastName);
            obj.put("dateOfBirth", dateOfBirth);
            obj.put("email", email);
            obj.put("type", "Client");

            clientService.updateEntity(obj.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void printAllBooks(BufferedReader bufferedReader) {
        try {
            if (bookService.getAllEntities().isCancelled())
                System.out.println("Operation timed out");
            JSONTokener tokener = new JSONTokener(bookService.getAllEntities().get());
            JSONArray array = new JSONArray(tokener);
            StringBuilder sb = new StringBuilder();
            for (Object objectAbs : array) {
                JSONObject object = (JSONObject) objectAbs;
                sb.append("ID: ").append(object.getLong("id"));
                sb.append(" Title: ").append(object.getString("title"));
                sb.append(" Author: ").append(object.getString("author"));
                sb.append(" Release Date: ").append(object.getString("date"));
                sb.append(" Price: ").append(object.getInt("price"));
                sb.append("\n");
            };
            System.out.println(sb.toString());


        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void printSortBy(BufferedReader bufferedReader) {
        try {
            System.out.println("For what class?");
            String className = bufferedReader.readLine();


            if (className.equals("Books")){
                printSortedBooks(bufferedReader);
            }
            else if (className.equals("Clients")){
                printSortedClients(bufferedReader);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void printAllClients(BufferedReader bufferedReader) {
        try {
            if (clientService.getAllEntities().isCancelled())
                System.out.println("Operation timed out");
            JSONTokener tokener = new JSONTokener(clientService.getAllEntities().get());
            JSONArray array = new JSONArray(tokener);
            StringBuilder sb = new StringBuilder();
            for (Object objectAbs : array) {
                JSONObject object = (JSONObject) objectAbs;
                sb.append("Date of birth: ").append(object.getString("dateOfBirth"));
                sb.append(" Email: ").append(object.getString("email"));
                sb.append(" First name: ").append(object.getString("firstName"));
                sb.append(" LastName: ").append(object.getString("lastName"));
                sb.append(" ID: ").append(object.getLong("id"));
                sb.append("\n");
            };
            System.out.println(sb.toString());


        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void deleteBook(BufferedReader bufferedReader) {
        try {
            System.out.println("ID: ");
            Long ID = Long.valueOf(bufferedReader.readLine());

            JSONObject object = new JSONObject();
            object.put("id", ID);
            object.put("type", "Book");
            bookService.removeEntity(object.toString());
            transactionService.removeRelationBook(object.toString());
        } catch (IOException v){
            v.printStackTrace();
        }
    }

    public void updateBook(BufferedReader bufferedReader) {
        try {
            System.out.println("ID: ");
            Long ID = Long.valueOf(bufferedReader.readLine());

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

            JSONObject object = new JSONObject();
            object.put("id", ID);
            object.put("category", category);
            object.put("title", title);
            object.put("author", author);
            object.put("date", date);
            object.put("price", price);
            object.put("type", "Book");

            bookService.updateEntity(object.toString());

        }catch (IOException v){
            v.printStackTrace();
        }
    }

    public void addClient(BufferedReader bufferedReader){
        try {
            System.out.println("ID: ");
            Long ID = Long.valueOf(bufferedReader.readLine());

            System.out.println("First Name: ");
            String firstName = bufferedReader.readLine();

            System.out.println("Last Name: ");
            String lastName = bufferedReader.readLine();

            System.out.println("Date Of Birth: ");
            String dateOfBirth = bufferedReader.readLine();

            System.out.println("Email: ");
            String email = bufferedReader.readLine();

            JSONObject obj = new JSONObject();

            obj.put("id", ID);
            obj.put("firstName", firstName);
            obj.put("lastName", lastName);
            obj.put("dateOfBirth", dateOfBirth);
            obj.put("email", email);
            obj.put("type", "Client");

            clientService.addEntity(obj.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteClient(BufferedReader bufferedReader) {
        try {
            System.out.println("ID: ");
            Long ID = Long.valueOf(bufferedReader.readLine());

            JSONObject object = new JSONObject();
            object.put("id", ID);
            object.put("type", "Client");
            clientService.removeEntity(object.toString());
            transactionService.removeRelationClient(object.toString());
        }catch (IOException v){
            v.printStackTrace();
        }
    }

    public void buyBook(BufferedReader bufferedReader) {
        try {

            System.out.println("Transaction ID: ");
            Long transactionID = Long.valueOf(bufferedReader.readLine());

            System.out.println("Book ID: ");
            Long bookID = Long.valueOf(bufferedReader.readLine());

            System.out.println("Client ID: ");
            Long clientID = Long.valueOf(bufferedReader.readLine());

            JSONObject obj = new JSONObject();

            obj.put("id", transactionID);
            obj.put("book_id", bookID);
            obj.put("client_id", clientID);
            obj.put("type", "Transaction");

            transactionService.addEntity(obj.toString());

        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    public void addBook(BufferedReader bufferedReader){
        try {
            System.out.println("ID: ");
            Long ID = Long.valueOf(bufferedReader.readLine());

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

            JSONObject obj = new JSONObject();

            obj.put("id", ID);
            obj.put("category", category);
            obj.put("title", title);
            obj.put("author", author);
            obj.put("date", date);
            obj.put("price", price);
            obj.put("type", "Book");

            bookService.addEntity(obj.toString());
        } catch (IOException v) {
            v.printStackTrace();
        }
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
        System.out.println("\t 14. Sort books based on the number of times it was bought");
        System.out.println("\t 15. Sort by:");
        System.out.println("Choose an option: ");
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
