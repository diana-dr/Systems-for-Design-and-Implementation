package socket.server.Repository.JDBC.DB;

import socket.server.Domain.BaseEntity;
import socket.server.Domain.Validators.Validator;
import socket.server.Domain.Validators.ValidatorException;
import socket.server.Repository.JDBC.Sort;
import socket.server.Repository.JDBC.SortingRepository;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

public abstract class DBRepository<ID extends Serializable,T extends BaseEntity<ID>> implements SortingRepository<ID,T> {
    protected static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";// System.getProperty("username");
    private static final String PASSWORD = "9d9d";
    protected Map<ID, T> entities;
    protected Validator<T> validator;
    protected Connection connection;
    protected String entityName;
    protected Class<T> entityType;

    public DBRepository(Validator<T> validator) {
        this.validator = validator;
        entities = new HashMap<>();
        setConnection();
    }

    public void setConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        return sort.sort(Arrays.asList(entities.values().toArray()));
    }

    @Override
    public Optional<T> findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<T> findAll() {
        return new HashSet<>(entities.values());
    }

    @Override
    public Optional<T> save(T entity) throws ValidatorException {
        if (entity == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<T> delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<T> update(T entity) throws ValidatorException {
        if (entity == null){
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k, v) -> entity));
    }

    public void loadData(){
        try {
            String sql = "select * from " + entityName;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                T newEntity = entityType.getDeclaredConstructor(ResultSet.class).newInstance(resultSet);
                entities.put(newEntity.getId(), newEntity);
            }
        }catch (InstantiationException | SQLException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
