package Domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client extends BaseEntity<Long> {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;

    public Client(){}

    public Client getEntity() { return this; }

    public Client(ResultSet resultSet) throws SQLException {
        setId(resultSet.getLong("id"));
        this.firstName = resultSet.getString("firstName");
        this.lastName = resultSet.getString("lastName");
        this.dateOfBirth = resultSet.getString("dob");
        this.email = resultSet.getString("email");
    }

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
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null || getClass() != object.getClass())
            return false;

        Client clients = (Client) object;

        return Objects.equals(firstName, clients.firstName) &&
                Objects.equals(lastName, clients.lastName) &&
                Objects.equals(dateOfBirth, clients.dateOfBirth) &&
                Objects.equals(email, clients.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(),firstName, lastName, dateOfBirth, email);
    }
}
