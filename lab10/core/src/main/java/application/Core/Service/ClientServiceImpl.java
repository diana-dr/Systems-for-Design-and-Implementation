package application.Core.Service;


import application.Core.Domain.Client;
import application.Core.Domain.Validators.ClientValidator;
import application.Core.Repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl extends EntityServiceImpl<Client> implements IClientService {

    public static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);
    ClientValidator validator = new ClientValidator();

    @Autowired
    public ClientServiceImpl(ClientRepository repo) {
        super(repo);
    }

    @Override
    @Transactional
    public Client updateClient(Long id, Client client) {
        log.trace("updateClient() --- method entered: client={}", client);

        validator.validate(client);

        Client update = repository.findById(id).orElse(client);
        update.setFirstName(client.getFirstName());
        update.setLastName(client.getLastName());
        update.setEmail(client.getEmail());
        update.setDateOfBirth(client.getDateOfBirth());

        log.trace("updateBook() --- method finished");
        return update;
    }
}
