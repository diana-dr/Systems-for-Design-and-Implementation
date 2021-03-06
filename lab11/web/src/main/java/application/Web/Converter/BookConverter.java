package application.Web.Converter;

import application.Core.Model.Book;
import application.Web.Dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class BookConverter extends AbstractConverterBaseEntityConverter<Book, BookDto> {

    @Override
    public Book convertDtoToModel(BookDto dto) {
        Book book = Book.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .releaseDate(dto.getReleaseDate())
                .price(dto.getPrice())
                .build();
        book.setId(dto.getId());
        return book;
    }

    @Override
    public BookDto convertModelToDto(Book book) {
        BookDto dto = BookDto.builder()
                .category(book.getCategory())
                .title(book.getTitle())
                .author(book.getAuthor())
                .releaseDate(book.getReleaseDate())
                .price(book.getPrice())
                .build();
        dto.setId(book.getId());
        return dto;
    }
}
