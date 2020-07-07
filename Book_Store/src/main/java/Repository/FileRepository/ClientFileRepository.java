package Repository.FileRepository;

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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class ClientFileRepository extends InMemoryRepository<Long, Client> {
    private String fileName;

    public ClientFileRepository(Validator<Client> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        loadData();
    }

    private void loadData() {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long id = Long.valueOf(items.get(0));
                String firstName = items.get(1);
                String lastName = items.get((2));
                String dateOfBirth = items.get((3));
                String email = items.get(4);

                Client client = new Client(firstName, lastName, dateOfBirth, email);
                client.setId(id);

                try {
                    super.save(client);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
                });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        Optional<Client> optional = super.save(entity);
        if (optional.isPresent()) {
            saveToFile();
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    @Override
    public Optional<Client> delete(Long aLong) {
        Optional<Client> optional = super.delete(aLong);
        if (optional.isPresent()) {
            saveToFile();
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException {
        Optional<Client> optional = super.update(entity);
        if (optional.isPresent()) {
            saveToFile();
            return optional;
        }
        saveToFile();
        return Optional.empty();
    }

    private void saveToFile() {
        Path path = Paths.get(fileName);

        Iterable<Client> clients = findAll();

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for (Client entity : clients) {
                bufferedWriter.write(
                        entity.getId() + "," + entity.getFirstName() + "," + entity.getLastName() + ","
                                + entity.getDateOfBirth() + "," + entity.getEmail());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
