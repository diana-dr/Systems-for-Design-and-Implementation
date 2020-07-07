package Repository.FileRepository;

import Domain.BaseEntity;
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


public class TransactionFileRepository extends InMemoryRepository<Long, Transaction> {

    private String fileName;

    public TransactionFileRepository(Validator<Transaction> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long transactionID = Long.valueOf(items.get(0));
                Long bookID = Long.valueOf(items.get(1));
                Long clientID = Long.valueOf(items.get(2));

                Transaction transaction = new Transaction(bookID, clientID);
                transaction.setId(transactionID);

                try {
                    super.save(transaction);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Transaction> save(Transaction entity) throws ValidatorException {
        Optional<Transaction> optional = super.save(entity);
        if (optional.isPresent()) {
            saveToFile();
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> delete(Long aLong) {
        Optional<Transaction> optional = super.delete(aLong);
        if (optional.isPresent()) {
            saveToFile();
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> update(Transaction entity) throws ValidatorException {
        Optional<Transaction> optional = super.update(entity);
        if (optional.isPresent()) {
            saveToFile();
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    private void saveToFile() {
        Path path = Paths.get(fileName);
        Iterable<Transaction> transactions = findAll();

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for (Transaction entity: transactions) {
                bufferedWriter.write(
                        entity.getId() + "," +entity.getBookID() + "," + entity.getClientID());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
