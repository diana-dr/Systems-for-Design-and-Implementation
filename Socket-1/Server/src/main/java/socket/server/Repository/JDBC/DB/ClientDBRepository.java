package socket.server.Repository.JDBC.DB;

import socket.server.Domain.Client;
import socket.server.Domain.Validators.Validator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class ClientDBRepository extends DBRepository<Long,Client> {
    public ClientDBRepository(Validator<Client> validator) {
        super(validator);
        entityName = "client";
        entityType = Client.class;
        loadData();
    }

    @Override
    public Optional<Client> save(Client client)  {
        if(client == null){
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(client);
        try{
            String sql = "insert into client(id,firstname,lastname,dob,email) values (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,client.getId());
            preparedStatement.setString(2,client.getFirstName());
            preparedStatement.setString(3,client.getLastName());
            preparedStatement.setString(4,client.getDateOfBirth());
            preparedStatement.setString(5,client.getEmail());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(entities.putIfAbsent(client.getId(), client));
    }

    @Override
    public Optional<Client> update(Client client){
        if (client == null){
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(client);
        try {
            String sql = "update client set firstname=?, lastname=?, dob=?, email=? where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,client.getFirstName());
            preparedStatement.setString(2,client.getLastName());
            preparedStatement.setString(3,client.getDateOfBirth());
            preparedStatement.setString(4,client.getEmail());
            preparedStatement.setLong(5,client.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return Optional.ofNullable(entities.computeIfPresent(client.getId(), (k, v) -> client));
    }

    @Override
    public Optional<Client> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        try {
            String sql = "delete from client where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(entities.remove(id));
    }
}
