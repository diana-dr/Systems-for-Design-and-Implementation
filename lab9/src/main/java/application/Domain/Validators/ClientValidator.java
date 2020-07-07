package application.Domain.Validators;

import application.Domain.Client;

import java.util.regex.Pattern;

public class ClientValidator implements Validator<Client> {
    private static Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}.\\d{2}.\\d{4}$");

    private static Validation<Client> correctID = SimpleValidation.from((client) -> client.getId() >= 1, "Invalid ID!");

    private static Validation<Client> correctAuthor = SimpleValidation.from((client) -> client.getFirstName().length() >= 4, "Client first name must contain at least 3 characters!");

    private static Validation<Client> correctTitle = SimpleValidation.from((client) -> client.getLastName().length() >= 4, "Client last name must contain at least 3 characters!");

    private static Validation<Client> correctDate = SimpleValidation.from((Client) -> DATE_PATTERN.matcher(Client.getDateOfBirth()).matches(), "Invalid date!");

    public void validate(Client client) throws ValidatorException {
        correctID.test(client).throwIfInvalid();
        correctAuthor.test(client).throwIfInvalid();
        correctTitle.test(client).throwIfInvalid();
        correctDate.test(client).throwIfInvalid();
    }
}
