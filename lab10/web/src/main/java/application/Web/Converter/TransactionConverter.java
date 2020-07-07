package application.Web.Converter;

import application.Core.Domain.Transaction;
import application.Web.Dto.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionConverter extends BaseConverter<Transaction, TransactionDto> {

    @Override
    public Transaction convertDtoToModel(TransactionDto dto) {
        Transaction transaction = Transaction.builder()
                .bookID(dto.getBookID())
                .clientID(dto.getClientID())
                .build();
        transaction.setId(dto.getId());
        return transaction;
    }

    @Override
    public TransactionDto convertModelToDto(Transaction transaction) {
        TransactionDto dto = TransactionDto.builder()
                .bookID(transaction.getBookID())
                .clientID(transaction.getClientID())
                .build();
        dto.setId(transaction.getId());
        return dto;
    }
}
