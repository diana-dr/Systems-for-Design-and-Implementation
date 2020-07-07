package application.Web.Converter;

import application.Core.Model.Client;
import application.Core.Model.Transaction;
import application.Web.Dto.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionConverter extends AbstractConverter<Transaction, TransactionDto> {

    @Override
    public Transaction convertDtoToModel(TransactionDto dto) {
        throw new RuntimeException("fuck!");
    }

    @Override
    public TransactionDto convertModelToDto(Transaction transaction) {
        return TransactionDto.builder()
                .bookID(transaction.getBook().getId())
                .clientID(transaction.getClient().getId())
                .build();
    }
}
