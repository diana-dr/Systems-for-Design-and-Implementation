package Repository.JDBC.DB;

import Domain.Book;
import Domain.Validators.Validator;
import Repository.JDBC.DB.DBRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class BookDBRepository extends DBRepository<Long,Book> {

    public BookDBRepository(Validator<Book> validator) {
        super(validator);
        entityName = "book";
        entityType = Book.class;
        loadData();
    }

    @Override
    public Optional<Book> save(Book book)  {
        if(book == null){
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(book);
        try{
            String sql = "insert into book(id, category, title, author, releaseDate, price) values (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,book.getId());
            preparedStatement.setString(2,book.getCategory());
            preparedStatement.setString(3,book.getTitle());
            preparedStatement.setString(4,book.getAuthor());
            preparedStatement.setString(5,book.getReleaseDate());
            preparedStatement.setInt(6,book.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(entities.putIfAbsent(book.getId(), book));
    }

    @Override
    public Optional<Book> update(Book book){
        if (book == null){
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(book);
        try {
            String sql = "update book set category=?, title=?, author=?, releaseDate=?, price=? where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,book.getCategory());
            preparedStatement.setString(2,book.getTitle());
            preparedStatement.setString(3,book.getAuthor());
            preparedStatement.setString(4,book.getReleaseDate());
            preparedStatement.setInt(5,book.getPrice());
            preparedStatement.setLong(6,book.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return Optional.ofNullable(entities.computeIfPresent(book.getId(), (k, v) -> book));
    }

    @Override
    public Optional<Book> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        try {
            String sql = "delete from book where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(entities.remove(id));
    }

}
