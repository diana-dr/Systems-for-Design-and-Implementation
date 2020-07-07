package application.Web.Dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class TransactionDto extends BaseDto {
    private Long bookID;
    private Long clientID;
}
