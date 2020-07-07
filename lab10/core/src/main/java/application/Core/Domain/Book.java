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

public class Book extends BaseEntity<Long> {

    private String category;
    private String title;
    private String author;
    private String releaseDate;
    private int price;
}
