package application.Web.Converter;

import application.Core.Model.BaseEntity;
import application.Core.Model.Client;
import application.Web.Dto.ClientDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ClientConverter extends AbstractConverterBaseEntityConverter<Client, ClientDto> {

    @Override
    public Client convertDtoToModel(ClientDto dto) {
        Client client = Client.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dateOfBirth(dto.getDateOfBirth())
                .email(dto.getEmail())
                .build();
        client.setId(dto.getId());
        return client;
    }

    @Override
    public ClientDto convertModelToDto(Client client) {
        ClientDto dto = ClientDto.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .dateOfBirth(client.getDateOfBirth())
                .email(client.getEmail())
                .books(client.getBooks().stream()
                        .map(BaseEntity::getId).collect(Collectors.toSet()))
                .build();
        dto.setId(client.getId());
        return dto;
    }
}
