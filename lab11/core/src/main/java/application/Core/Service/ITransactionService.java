package application.Core.Service;

import application.Core.Model.Transaction;

import java.util.Set;

public interface ITransactionService {
    Set<Transaction> getAllTransactionsForClient(Long clientID);

    void addTransaction(Long clientID, Long bookID);

    void updateTransaction(Long clientID, Long bookID);

    void deleteTransaction(Long clientID, Long bookID);
}
