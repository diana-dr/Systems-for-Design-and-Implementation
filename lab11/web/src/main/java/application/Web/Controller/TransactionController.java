package application.Web.Controller;

import application.Core.Model.Book;
import application.Core.Model.Client;
import application.Core.Model.Transaction;
import application.Core.Service.IBookService;
import application.Core.Service.IClientService;
import application.Core.Service.ITransactionService;
import application.Web.Converter.TransactionConverter;
import application.Web.Dto.ClientDto;
import application.Web.Dto.EmptyJsonResponse;
import application.Web.Dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
public class TransactionController {
    public static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private TransactionConverter transactionConverter;


    @RequestMapping(value = "/transactions/{clientID}", method = RequestMethod.GET)
    public Set<TransactionDto> getClientTransactions(@PathVariable final Long clientID) {
        log.trace("getClientTransactions() --- method entered");
        Set<Transaction> transactions = transactionService.getAllTransactionsForClient(clientID);
        Set<TransactionDto> transactionDtos = new HashSet<>(transactionConverter.convertModelsToDtos(transactions));
        log.trace("getClientTransactions() --- transactions={}", transactions);
        return transactionDtos;
    }

    @RequestMapping(value = "/transactions/{clientID}", method = RequestMethod.POST)
    ResponseEntity<?> saveTransaction(@PathVariable final Long clientID,
                                   @RequestBody final Long bookID) {
        log.trace("saveTransaction() --- method entered: clientID {}, bookID {}", clientID, bookID);
        transactionService.addTransaction(clientID, bookID);
        log.trace("saveTransaction() --- method finished");
        return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.PUT)
    public ResponseEntity updateTransaction(@RequestBody TransactionDto transaction) {
        log.trace("updateTransaction() --- method entered updateTransaction {}", transaction);
        transactionService.updateTransaction(transaction.getClientID(), transaction.getBookID());
        log.trace("updateRental() --- method exit");
        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }
}
