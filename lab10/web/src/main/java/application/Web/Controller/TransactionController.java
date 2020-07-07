package application.Web.Controller;

import application.Web.Converter.TransactionConverter;
import application.Core.Service.ITransactionService;
import application.Web.Dto.BookDto;
import application.Web.Dto.BooksDto;
import application.Web.Dto.TransactionDto;
import application.Web.Dto.TransactionsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {
    public static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private TransactionConverter transactionConverter;


    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    TransactionsDto getTransactions() {
        log.trace("getTransactions() --- method entered");
        TransactionsDto transactions = new TransactionsDto(transactionConverter.convertModelsToDtos(transactionService.getAllEntities()));
        log.trace("getTransactions() --- method finished: transactions={}", transactions);
        return transactions;
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    TransactionDto saveTransaction(@RequestBody TransactionDto transactionDto) {
        log.trace("saveTransaction() --- method entered: transaction={}", transactionDto);
        TransactionDto transaction = transactionConverter.convertModelToDto(transactionService.addEntity(
                transactionConverter.convertDtoToModel(transactionDto)));
        log.trace("saveTransaction() --- method finished: transaction={}", transaction);
        return transaction;
    }

    @RequestMapping(value = "/transactions/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@PathVariable Long id) {
        log.trace("deleteTransaction() --- method entered: transactionID={}", id);
        transactionService.removeEntity(id);
        log.trace("deleteTransaction() --- method finished");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

