package Repository;

import Domain.BaseEntity;
import Domain.Client;
import Domain.Validators.ClientValidator;
import Domain.Validators.Validator;
import Repository.FileRepository.ClientFileRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class ClientFileRepositoryTest extends BaseEntity {

    Validator<Client> validator;
    ClientFileRepository memoryRepository;
    String fileName;
    Client client1;
    Client client2;

    @Before
    public void setUp() {
        fileName = "./data/clients.txt";
        validator = new ClientValidator();
        memoryRepository = new ClientFileRepository(validator, fileName);

        Client client1 = new Client("fname", "lname", "12.12.1212", "email1");
        client1.setId(3L);
        Client client2 = new Client("fname2", "lname2", "12.12.1212", "email2");
        client2.setId(3L);
    }

    @Test
    public void save() throws Exception {
        memoryRepository.save(client1);
        Optional<Client> opt = Optional.ofNullable(memoryRepository.findOne(3L).orElseThrow(() -> new Exception("Not found")));
        opt.ifPresent(book -> assertEquals(client1, book));
    }

    @Test
    public void delete() throws Exception {
        memoryRepository.save(client2);
        memoryRepository.delete(3L);
        assertTrue(!memoryRepository.findOne(3L).isPresent());
    }

    @Test
    public void update() throws Exception {

        memoryRepository.save(client1);
        memoryRepository.update(client2);

        Optional<Client> opt = Optional.ofNullable(memoryRepository.findOne(3L).orElseThrow(() -> new Exception("Not found")));
        opt.ifPresent(book -> assertEquals(client2, book));
    }
}