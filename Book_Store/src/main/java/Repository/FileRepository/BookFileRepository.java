package Repository.FileRepository;

import Domain.Book;
import Domain.Client;
import Domain.Transaction;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repository.InMemoryRepository;
import Repository.Repository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class BookFileRepository extends InMemoryRepository<Long, Book> {

    private String fileName;

    public BookFileRepository(Validator<Book> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        loadData();
    }

    private void loadData() {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                String category = items.get(1);
                Long id = Long.valueOf(items.get(0));
                String title = items.get(2);
                String author = items.get((3));
                String releaseDate = items.get((4));
                int price = Integer.parseInt(items.get(5));

                Book book = new Book(category,title, author, releaseDate, price);
                book.setId(id);

                try {
                    super.save(book);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Book> save(Book entity) throws ValidatorException {
        Optional<Book> optional = super.save(entity);
        if (optional.isPresent()) {
            saveToFile();
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    @Override
    public Optional<Book> delete(Long aLong) {
        Optional<Book> optional = super.delete(aLong);

        if (optional.isPresent()) {
            saveToFile();
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    @Override
    public Optional<Book> update(Book entity) throws ValidatorException {
        Optional<Book> optional = super.update(entity);
        if (optional.isPresent()) {
            saveToFile();
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    private void saveToFile() {
        Path path = Paths.get(fileName);
        Iterable<Book> books = findAll();

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for (Book entity: books) {
                bufferedWriter.write(
                        entity.getId() + "," + entity.getCategory() + "," + entity.getTitle() + "," + entity.getAuthor() + "," + entity.getReleaseDate() + "," + entity.getPrice());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
