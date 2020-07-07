package application.Service;

import application.Domain.Client;
import application.Repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ClientServiceImpl extends EntityServiceImpl<Client> implements IClientService {

    public static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    public ClientServiceImpl(ClientRepository repo) {
        super(repo);
    }

    @Override
    @Transactional
    public void updateClient(Client client) {
        log.trace("updateClient - method entered: client={}", client);
        repository.findById(client.getId())
                .ifPresent(c -> {
                    c.setFirstName(client.getFirstName());
                    c.setLastName(client.getLastName());
                    c.setEmail(client.getEmail());
                    c.setDateOfBirth(client.getDateOfBirth());
                    log.debug("updateClient - updated: c={}", c);
                });
        log.trace("updateBook - method finished");
    }
}
