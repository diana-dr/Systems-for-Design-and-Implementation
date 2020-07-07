package socket.client;

import socket.client.Service.BookService;
import socket.client.Service.ClientService;
import socket.client.Service.TransactionService;
import socket.client.TCP.TcpClient;
import socket.client.UI.ConsoleUI;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            TcpClient tcpClient = new TcpClient();

            ConsoleUI console = new ConsoleUI(new BookService(executorService, tcpClient), new ClientService(executorService, tcpClient), new TransactionService(executorService, tcpClient));
            console.runConsole();
        } catch (Exception ex) {System.out.println(ex.getMessage());}

    }
}

