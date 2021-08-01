package hu.tamas.splendex.controller;

import hu.tamas.splendex.service.BankTransactionService;
import hu.tamas.splendex.util.SystemKeys;
import hu.tamas.splendex.util.exception.BankTransactionException;
import hu.tamas.splendex.util.exception.InvoiceNotFoundException;
import hu.tamas.splendex.util.exception.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final BankTransactionService bankTransactionService;

    @RequestMapping(value = "/deposit", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> depositTransaction(@RequestParam(value = "personId") final Long personId,
                                                     @RequestParam(value = "amount") final Long amount) {
        try {
            log.info("Start deposit");
            this.bankTransactionService.deposit(personId, amount);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_DEPOSIT, HttpStatus.OK);
        } catch (InvoiceNotFoundException e) {
            log.error("deposit - InvoiceNotFoundException", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.FAILED_DEPOSIT + " " + e.getMessage(), HttpStatus.OK);
        } catch (PersonNotFoundException e) {
            log.error("deposit - PersonNotFoundException", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.FAILED_DEPOSIT + " " + e.getMessage(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> withdrawTransaction(@RequestParam(value = "personId") final Long personId,
                                                      @RequestParam(value = "amount") final Long amount) {
        try {
            log.info("Start withdraw");
            this.bankTransactionService.withdraw(personId, amount);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_WITHDRAWAL, HttpStatus.OK);
        } catch (InvoiceNotFoundException e) {
            log.error("withdraw - InvoiceNotFoundException", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.FAILED_WITHDRAWAL + " " + e.getMessage(), HttpStatus.OK);
        } catch (PersonNotFoundException e) {
            log.error("withdraw - PersonNotFoundException", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.FAILED_WITHDRAWAL + " " + e.getMessage(), HttpStatus.OK);
        } catch (BankTransactionException e) {
            log.error("withdraw - BankTransactionException", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.FAILED_WITHDRAWAL + " " + e.getMessage(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> transfer(@RequestParam(value = "fromPersonId") final Long fromPersonId,
                                           @RequestParam(value = "toPersonId") final Long toPersonId,
                                           @RequestParam(value = "amount") final Long amount) {
        try {
            log.info("Start transfer");
            this.bankTransactionService.transfer(fromPersonId, toPersonId, amount);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_TRANSFER, HttpStatus.OK);
        } catch (InvoiceNotFoundException e) {
            log.error("transfer - InvoiceNotFoundException", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.FAILED_TRANSFER + " " + e.getMessage(), HttpStatus.OK);
        } catch (PersonNotFoundException e) {
            log.error("transfer - PersonNotFoundException", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.FAILED_TRANSFER + " " + e.getMessage(), HttpStatus.OK);
        } catch (BankTransactionException e) {
            log.error("transfer - BankTransactionException", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.FAILED_TRANSFER + " " + e.getMessage(), HttpStatus.OK);
        }
    }

}
