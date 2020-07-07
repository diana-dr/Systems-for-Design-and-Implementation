package socket.server.Domain.Validators;

import socket.server.Domain.Book;
import java.util.regex.Pattern;

public class BookValidator implements Validator<Book>{
    private static Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}.\\d{2}.\\d{4}$");

    private static Validation<Book> correctID = SimpleValidation.from((book) -> book.getId() >= 1, "Invalid ID!");

    private static Validation<Book> correctAuthor = SimpleValidation.from((book) -> book.getAuthor().length() >= 3, "Invalid author name!");

    private static Validation<Book> correctTitle = SimpleValidation.from((book) -> book.getTitle().length() >= 3, "Invalid title!");

    private static Validation<Book> correctPrice = SimpleValidation.from((book) -> book.getPrice() > 0, "Price needs to be greater than 0!");

    private static Validation<Book> correctDate = SimpleValidation.from((book) -> DATE_PATTERN.matcher(book.getReleaseDate()).matches(), "Invalid date!");

    public void validate(Book book) throws ValidatorException {
        correctID.test(book).throwIfInvalid();
        correctAuthor.test(book).throwIfInvalid();
        correctTitle.test(book).throwIfInvalid();
        correctPrice.test(book).throwIfInvalid();
        correctDate.test(book).throwIfInvalid();
    }
}
