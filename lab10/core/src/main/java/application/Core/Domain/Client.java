package application.Core.Domain;

import lombok.*;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder

public class Client extends BaseEntity<Long> {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
}
