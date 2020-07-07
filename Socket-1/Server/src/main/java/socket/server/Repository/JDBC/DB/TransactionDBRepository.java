package socket.server.Repository.JDBC.DB;

import socket.server.Domain.BaseEntity;
import socket.server.Domain.Client;
import socket.server.Domain.Transaction;
import socket.server.Domain.Validators.Validator;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionDBRepository extends DBRepository<Long,Transaction> {
    public TransactionDBRepository(Validator<Transaction> validator) {
        super(validator);
        entityName = "transaction";
        entityType = Transaction.class;
    }

    @Override
    public Optional<Transaction> save(Transaction transaction)  {
        if(transaction == null){
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(transaction);
        try{
            String sql = "insert into transaction(id,client_id,book_id) values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,transaction.getId());
            preparedStatement.setLong(2,transaction.getClientID());
            preparedStatement.setLong(3,transaction.getBookID());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(entities.putIfAbsent(transaction.getId(), transaction));
    }

    @Override
    public Optional<Transaction> update(Transaction transaction){
        if (transaction == null){
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(transaction);
        try {
            String sql = "update transaction set client_id=?, book_id=? where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,transaction.getClientID());
            preparedStatement.setLong(2,transaction.getBookID());
            preparedStatement.setLong(3,transaction.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return Optional.ofNullable(entities.computeIfPresent(transaction.getId(), (k, v) -> transaction));
    }

    @Override
    public Optional<Transaction> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        try {
            String sql = "delete from transaction where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(entities.remove(id));
    }
}
