package application.Web.Controller;

import application.Core.Model.Client;
import application.Core.Service.IClientService;
import application.Web.Converter.ClientConverter;
import application.Web.Dto.ClientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
public class ClientController {
    public static final Logger log = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private IClientService clientService;

    @Autowired
    private ClientConverter clientConverter;


    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    Set<ClientDto> getClients() {
        log.trace("getClients() --- method entered");
        Set<Client> clients = clientService.getAllClients();
        Set<ClientDto> clientDtos = new HashSet<>(clientConverter.convertModelsToDtos(clients));
        log.trace("getClients() --- method finished: clients={}", clientDtos);
        return clientDtos;
    }

    @RequestMapping(value = "/clients/{id}", method = RequestMethod.GET)
    ClientDto getClient(@PathVariable final Long id) {
        log.trace("getClient() --- method entered clientId={}", id);
        Client client = clientService.getClientyByID(id);
        ClientDto result = clientConverter.convertModelToDto(client);
        log.trace("getClient() --- method finished: client={}", result);
        return result;
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    ClientDto saveClient(@RequestBody ClientDto clientDto) {
        log.trace("saveClient() --- method entered: client={}", clientDto);
        ClientDto client = clientConverter.convertModelToDto(clientService.addClient(
                clientConverter.convertDtoToModel(clientDto)));
        log.trace("saveClient() --- method finished: client={}", client);
        return client;
    }

    @RequestMapping(value = "/clients/{id}", method = RequestMethod.PUT)
    ClientDto updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) {
        log.trace("updateClient() --- method entered: client={}", clientDto);
        ClientDto client = clientConverter.convertModelToDto(clientService.updateClient(id,
                clientConverter.convertDtoToModel(clientDto)));
        log.trace("updateClient() --- method finished");
        return client;
    }

    @RequestMapping(value = "/clients/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteClient(@PathVariable Long id){
        log.trace("deleteClient() --- method entered: clientID={}", id);
        clientService.removeClient(id);
        log.trace("deleteClient() --- method finished");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/clients/sorted/{direction}/{args}", method = RequestMethod.GET)
    Set<ClientDto> sort(@PathVariable String direction, @PathVariable String args) {
        log.trace("sort() -- method entered: direction={}, args={}", direction, args);
        Set<Client> clients = clientService.sort(direction, args);

        Set<ClientDto> clientDtos = new HashSet<>(clientConverter.convertModelsToDtos(clients));
        log.trace("sort() -- method finished: clients {}", clientDtos);
        return clientDtos;
    }
}

