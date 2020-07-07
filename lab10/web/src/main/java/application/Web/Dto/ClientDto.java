package application.Web.Dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class ClientDto extends BaseDto {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
}
