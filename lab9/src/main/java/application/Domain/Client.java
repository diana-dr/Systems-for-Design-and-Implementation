package application.Domain;

import javax.persistence.Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Entity
public class Client extends BaseEntity<Long> {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;

    public Client(){}

    public Client getEntity() { return this; }

    public Client(String firstNameN, String lastName, String dateOfBirth, String email) {
        this.firstName = firstNameN;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + "\n" +
                "Last Name: " + lastName + "\n" +
                "Date Of Birth: " + dateOfBirth + "\n" +
                "Email: " + email + "\n"
                + super.toString();
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long aLong) {

        super.setId(aLong);
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return firstName.equals(client.firstName) &&
                lastName.equals(client.lastName) &&
                dateOfBirth.equals(client.dateOfBirth) &&
                email.equals(client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dateOfBirth, email);
    }
}
