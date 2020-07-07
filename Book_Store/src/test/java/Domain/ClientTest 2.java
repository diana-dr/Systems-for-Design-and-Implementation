package Domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientTest {

    Client client;

    @Before
    public void setUp() throws Exception {
        client = new Client("firstName","lastName","02.02.2009","a@a.com");
        client.setId((long) 13); // lucky
    }

    @Test
    public void getId() {
        assertEquals(13, (long) client.getId());
    }

    @Test
    public void setId() {
        client.setId((long) 7);
        assertEquals(7, (long) client.getId());
    }

    @Test
    public void getEmail() {
        assertEquals("a@a.com", client.getEmail());
    }

    @Test
    public void setEmail() {
        client.setEmail("b@b.com");
        assertEquals("b@b.com", client.getEmail());
    }

    @Test
    public void getReleaseDate() {
        assertEquals("02.02.2009", client.getDateOfBirth());
    }

    @Test
    public void setReleaseDate() {
        client.setDateOfBirth("02.02.2001");
        assertEquals("02.02.2001", client.getDateOfBirth());
    }

    @Test
    public void getFirstName() {
        assertEquals("firstName", client.getFirstName());
    }

    @Test
    public void setFirstName() {
        client.setFirstName("firstName2");
        assertEquals("firstName2", client.getFirstName());
    }

    @Test
    public void getLastName() {
        assertEquals("lastName", client.getLastName());
    }

    @Test
    public void setLastName() {
        client.setLastName("lastName2");
        assertEquals("lastName2", client.getLastName());
    }
}
