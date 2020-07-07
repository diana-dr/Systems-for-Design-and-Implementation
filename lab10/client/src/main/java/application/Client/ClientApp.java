package application.Client;

import application.Client.UI.Console;
import application.Web.Dto.TransactionsDto;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

public class ClientApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        "application.client.Config"
                );
        Console console = new Console(context);
        console.runConsole();
    }
}
