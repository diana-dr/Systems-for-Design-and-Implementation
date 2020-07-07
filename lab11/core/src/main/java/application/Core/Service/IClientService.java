package application.Core.Service;

import application.Core.Model.Book;
import application.Core.Model.Client;
import application.Core.Model.Validators.ValidatorException;

import java.util.Set;

public interface IClientService {
    Client updateClient(Long id, Client client);
    Client addClient(Client entity);
    Set<Client> getAllClients();
    void removeClient(long id);
    Client getClientyByID(long id) throws ValidatorException;
    Set<Client> sort(String direc, String args);

    void addTransaction(Client client, Book book);
}
