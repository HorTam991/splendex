package hu.tamas.splendex.service;

import hu.tamas.splendex.model.BankTransaction;
import hu.tamas.splendex.model.core.ListResponse;
import hu.tamas.splendex.util.exception.BankTransactionException;
import hu.tamas.splendex.util.exception.InvoiceNotFoundException;
import hu.tamas.splendex.util.exception.PersonNotFoundException;

import java.util.List;
import java.util.Map;

public interface BankTransactionService {

    void deposit(Long personId, Long amount) throws PersonNotFoundException, InvoiceNotFoundException;

    void withdraw(Long personId, Long amount) throws PersonNotFoundException, InvoiceNotFoundException, BankTransactionException;

    void transfer(Long fromPersonId, Long toPersonId, Long amount) throws PersonNotFoundException, InvoiceNotFoundException, BankTransactionException;

    List<BankTransaction> getMyBankTransactionLog(Long personId) throws PersonNotFoundException;

    ListResponse getMyBankTransactions(Long personId, Map<String, String> filterParams, int pageIndex, int pageSize) throws PersonNotFoundException;

    byte[] exportMyBankTransactions(Long personId, Map<String, String> filterParams);

}
