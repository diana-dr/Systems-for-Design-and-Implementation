package application.Config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"application.Repository", "application.Service", "application.UI"})
public class BookstoreConfig {

}
