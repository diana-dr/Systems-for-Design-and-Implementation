package application.Core.Service;

import application.Core.Domain.Transaction;

public interface ITransactionService extends IEntityService<Transaction> {
    void removeRelationClient(long clientId);
    void removeRelationBook(long bookID);
}
