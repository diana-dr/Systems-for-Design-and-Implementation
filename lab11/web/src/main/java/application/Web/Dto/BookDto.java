package application.Web.Dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookDto extends BaseDto {
    private String category;
    private String title;
    private String author;
    private String releaseDate;
    private int price;
}
