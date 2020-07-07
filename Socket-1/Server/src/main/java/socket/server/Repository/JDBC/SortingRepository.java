package socket.server.Repository.JDBC;

import socket.server.Domain.BaseEntity;
import socket.server.Repository.Repository;

import java.io.Serializable;

public interface SortingRepository<ID extends Serializable, T extends BaseEntity<ID>>
                                            extends Repository<ID, T> {

    Iterable<T> findAll(Sort sort);

    //TODO: insert sorting-related code here
}
