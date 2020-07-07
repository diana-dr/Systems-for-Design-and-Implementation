package application.Web.Dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ClientDto extends BaseDto {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private Set<Long> books;
}
