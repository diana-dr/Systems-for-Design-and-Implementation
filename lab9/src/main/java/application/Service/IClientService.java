package application.Service;

import application.Domain.Client;
import application.Domain.Validators.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface IClientService extends IEntityService<Client> {
    void updateClient(Client client);
}
