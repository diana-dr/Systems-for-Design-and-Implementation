package application.Core.Service;

import application.Core.Model.Book;
import application.Core.Model.Client;
import application.Core.Model.Transaction;
import application.Core.Model.Validators.ClientValidator;
import application.Core.Model.Validators.ValidatorException;
import application.Core.Repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class ClientServiceImpl implements IClientService {

    public static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);
    ClientValidator validator = new ClientValidator();

    @Autowired
    private ClientRepository repository;


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
        update.setClientTransactions(client.getClientTransactions());
        log.trace("updateBook() --- method finished");
        return update;
    }

    @Override
    public Client addClient(Client entity) {
        log.trace("addEntity - method entered: entity={}", entity);
//        validator.validate(entity);
        repository.save(entity);
        log.trace("addEntity - method finished");
        return entity;
    }

    @Override
    public Set<Client> getAllClients() {
        log.trace("getAllEntities --- method entered");

        Iterable<Client> entities = repository.findAll();
        Set<Client> entitiesSet = new HashSet<>();
        entities.forEach(entitiesSet::add);

        log.trace("getAllEntities: result={}", entitiesSet);
        return entitiesSet;
    }

    @Override
    public void removeClient(long id) {
        Client entity = getClientyByID(id);
        log.trace("deleteEntity() --- method entered: entity={}", entity);
        repository.deleteById(id);
        log.trace("deleteEntity() --- deleted: entity={}", entity);
    }

    @Override
    public Client getClientyByID(long id) throws ValidatorException {
        log.trace("getEntityByID() --- method entered: entityID={}", id);
        if (repository.findById(id).isEmpty())
            throw new ValidatorException("Missing entity with ID: " + id);
        Client entity = repository.findById(id).get();
        log.trace("getEntityByID() --- method finished: entity={}", entity);
        return entity;
    }

    @Override
    public Set<Client> sort(String direc, String args) {
        log.trace("sort() --- method entered: direction={}, args={}", direc, args);
        Set<Client> result = new HashSet<>();
        if (direc.equals("ASC")) {
            Iterable<Client> entities = repository.findAll(Sort.by(Sort.Direction.ASC, args));
            entities.forEach(result::add);
        } else if (direc.equals("DESC")) {
            Iterable<Client> entities = repository.findAll(Sort.by(Sort.Direction.DESC, args));
            entities.forEach(result::add); }

        log.trace("sort() --- method finished: result={}", result);
        return result;
    }

    @Override
    public void addTransaction(Client client, Book book) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        client.addTransaction(book, formatter.format(date));
        updateClient(client.getId(), client);
    }
}
