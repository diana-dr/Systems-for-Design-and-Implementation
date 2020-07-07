package application.Core.Service;


import application.Core.Domain.Client;

public interface IClientService extends IEntityService<Client> {
    Client updateClient(Long id, Client client);
}
