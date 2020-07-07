package Service;

import Domain.Client;
import Repository.Repository;

public class ClientService extends EntityService<Client>{

    public ClientService(Repository<Long, Client> repo) {
        super(repo);
    }
}
