package application.Core.Model;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TransactionPK implements Serializable {
    private Book book;
    private Client client;
}
