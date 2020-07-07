package Domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BooksTest {

    Book book;

    @Before
    public void setUp() throws Exception {
        book = new Book("kids","testTitle","testAuthor","02.02.2009",100);
        book.setId((long) 13); // lucky
    }

    @Test
    public void getId() {
        assertEquals(13, (long) book.getId());
    }

    @Test
    public void setId() {
        book.setId((long) 7);
        assertEquals(7, (long) book.getId());
    }

    @Test
    public void getCategory() {
        assertEquals("kids", book.getCategory());
    }

    @Test
    public void setCategory() {
        book.setCategory("definitely not");
        assertEquals("definitely not", book.getCategory());
    }

    @Test
    public void getPrice() {
        assertEquals(100, book.getPrice());
    }

    @Test
    public void setPrice() {
        book.setPrice(200);
        assertEquals(200, book.getPrice());
    }

    @Test
    public void getReleaseDate() {
        assertEquals("02.02.2009", book.getReleaseDate());
    }

    @Test
    public void setReleaseDate() {
        book.setReleaseDate("02.02.2001");
        assertEquals("02.02.2001", book.getReleaseDate());
    }

    @Test
    public void getAuthor() {
        assertEquals("testAuthor", book.getAuthor());
    }

    @Test
    public void setAuthor() {
        book.setAuthor("testAuthor2");
        assertEquals("testAuthor2", book.getAuthor());
    }

    @Test
    public void getTitle() {
        assertEquals("testTitle", book.getTitle());
    }

    @Test
    public void setTitle() {
        book.setTitle("testTitle2");
        assertEquals("testTitle2", book.getTitle());
    }
}