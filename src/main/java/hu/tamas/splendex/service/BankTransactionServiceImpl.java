package hu.tamas.splendex.service;

import hu.tamas.splendex.model.BankTransaction;
import hu.tamas.splendex.model.Invoice;
import hu.tamas.splendex.model.Person;
import hu.tamas.splendex.model.core.ListResponse;
import hu.tamas.splendex.repository.BankTransactionRepository;
import hu.tamas.splendex.repository.InvoiceRepository;
import hu.tamas.splendex.repository.PersonRepository;
import hu.tamas.splendex.util.SystemKeys;
import hu.tamas.splendex.util.XLSUtil;
import hu.tamas.splendex.util.exception.BankTransactionException;
import hu.tamas.splendex.util.exception.InvoiceNotFoundException;
import hu.tamas.splendex.util.exception.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BankTransactionServiceImpl implements BankTransactionService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BankTransactionRepository bankTransactionRepository;

    private final PersonRepository personRepository;

    private final InvoiceRepository invoiceRepository;

    @Override
    public void deposit(Long personId, Long amount) throws PersonNotFoundException, InvoiceNotFoundException {
        Optional<Person> person = this.personRepository.findById(personId);
        if (!person.isPresent()) {
            throw new PersonNotFoundException(SystemKeys.ExceptionTexts.TARGET_PERSON_NOT_FOUND);
        }

        Invoice invoice = this.invoiceRepository.findByPersonId(personId);
        if (invoice == null) {
            throw new InvoiceNotFoundException(SystemKeys.ExceptionTexts.TARGET_PERSON_DONT_HAVE_INVOICE);
        }

        Long oldActualAmount = invoice.getActualInvoiceTotal();
        Long actualAmount = invoice.getActualInvoiceTotal();
        actualAmount = actualAmount + amount;
        invoice.setActualInvoiceTotal(actualAmount);
        this.invoiceRepository.save(invoice);

        bankTransactionRepository.save(createBankTransaction(personId, invoice.getInvoiceNumber(),
                personId, invoice.getInvoiceNumber(),
                amount, SystemKeys.BankTransactionTypes.DEPOSIT,
                oldActualAmount, actualAmount, personId));
    }

    @Override
    public void withdraw(Long personId, Long amount) throws PersonNotFoundException, InvoiceNotFoundException, BankTransactionException {
        Optional<Person> person = this.personRepository.findById(personId);
        if (!person.isPresent()) {
            throw new PersonNotFoundException(SystemKeys.ExceptionTexts.TARGET_PERSON_NOT_FOUND);
        }

        Invoice invoice = this.invoiceRepository.findByPersonId(personId);
        if (invoice == null) {
            throw new InvoiceNotFoundException(SystemKeys.ExceptionTexts.TARGET_PERSON_DONT_HAVE_INVOICE);
        }

        Long oldActualAmount = invoice.getActualInvoiceTotal();
        Long actualAmount = invoice.getActualInvoiceTotal();
        if (actualAmount - amount < 0) {
            throw new BankTransactionException(SystemKeys.ExceptionTexts.YOU_DONT_HAVE_ENOUGH_MONEY_TO_TRANSFER);
        }
        actualAmount = actualAmount - amount;
        invoice.setActualInvoiceTotal(actualAmount);
        this.invoiceRepository.save(invoice);

        bankTransactionRepository.save(createBankTransaction(personId, invoice.getInvoiceNumber(),
                personId, invoice.getInvoiceNumber(),
                amount, SystemKeys.BankTransactionTypes.WITHDRAWAL,
                oldActualAmount, actualAmount, personId));
    }

    @Override
    public void transfer(Long fromPersonId, Long toPersonId, Long amount) throws PersonNotFoundException, InvoiceNotFoundException, BankTransactionException {
        if (fromPersonId.equals(toPersonId)) {
            throw new BankTransactionException(SystemKeys.ExceptionTexts.IT_CANNOT_BE_THE_SAME_SENDER_AND_TARGET);
        }

        Optional<Person> toPerson = this.personRepository.findById(toPersonId);
        if (!toPerson.isPresent()) {
            throw new PersonNotFoundException(SystemKeys.ExceptionTexts.TARGET_PERSON_NOT_FOUND);
        }

        Invoice fromInvoice = this.invoiceRepository.findByPersonId(fromPersonId);
        if (fromInvoice == null) {
            throw new InvoiceNotFoundException(SystemKeys.ExceptionTexts.YOU_DONT_HAVE_INVOICE);
        }

        Invoice toInvoice = this.invoiceRepository.findByPersonId(toPersonId);
        if (toInvoice == null) {
            throw new InvoiceNotFoundException(SystemKeys.ExceptionTexts.TARGET_PERSON_DONT_HAVE_INVOICE);
        }

        Long oldActualAmount = fromInvoice.getActualInvoiceTotal();
        Long actualAmountFrom = fromInvoice.getActualInvoiceTotal();
        if (actualAmountFrom - amount < 0) {
            throw new BankTransactionException(SystemKeys.ExceptionTexts.YOU_DONT_HAVE_ENOUGH_MONEY_TO_TRANSFER);
        }
        actualAmountFrom = actualAmountFrom - amount;
        fromInvoice.setActualInvoiceTotal(actualAmountFrom);
        this.invoiceRepository.save(fromInvoice);

        Long oldActualAmountTo = toInvoice.getActualInvoiceTotal();
        Long actualAmountTo = toInvoice.getActualInvoiceTotal();
        actualAmountTo = actualAmountTo + amount;
        toInvoice.setActualInvoiceTotal(actualAmountTo);
        this.invoiceRepository.save(toInvoice);

        bankTransactionRepository.save(createBankTransaction(fromPersonId, fromInvoice.getInvoiceNumber(),
                toPersonId, toInvoice.getInvoiceNumber(),
                amount, SystemKeys.BankTransactionTypes.TRANSFER,
                oldActualAmount, actualAmountFrom, fromPersonId));

        bankTransactionRepository.save(createBankTransaction(fromPersonId, fromInvoice.getInvoiceNumber(),
                toPersonId, toInvoice.getInvoiceNumber(),
                amount, SystemKeys.BankTransactionTypes.TRANSFER,
                oldActualAmountTo, actualAmountTo, toPersonId));
    }

    private BankTransaction createBankTransaction(Long fromPersonId, String fromInvoiceNumber,
                                                  Long toPersonId, String toInvoiceNumber,
                                                  Long amount, Long transactionType,
                                                  Long oldActualTotal, Long newActualTotal,
                                                  Long personId) {
        BankTransaction transaction = new BankTransaction();
        transaction.setFromPersonId(fromPersonId);
        transaction.setFromInvoiceNumber(fromInvoiceNumber);
        transaction.setToPersonId(toPersonId);
        transaction.setToInvoiceNumber(toInvoiceNumber);
        transaction.setTransactionAmount(amount);
        transaction.setTransactionTypeId(transactionType);
        transaction.setTransactionDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        transaction.setOldInvoiceTotal(oldActualTotal);
        transaction.setNewInvoiceTotal(newActualTotal);
        transaction.setPersonId(personId);
        return transaction;
    }

    @Override
    public List<BankTransaction> getMyBankTransactionLog(Long personId) throws PersonNotFoundException {
        Optional<Person> toPerson = this.personRepository.findById(personId);
        if (!toPerson.isPresent()) {
            throw new PersonNotFoundException(SystemKeys.ExceptionTexts.PERSON_NOT_FOUND);
        }
        return this.bankTransactionRepository.findAllByFromPersonIdOrToPersonIdOrderByTransactionDate(personId, personId);
    }

    @Override
    public ListResponse getMyBankTransactions(Long personId, Map<String, String> filterParams, int pageIndex, int pageSize) throws PersonNotFoundException {
        Optional<Person> toPerson = this.personRepository.findById(personId);
        if (!toPerson.isPresent()) {
            throw new PersonNotFoundException(SystemKeys.ExceptionTexts.PERSON_NOT_FOUND);
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BankTransaction> query = criteriaBuilder.createQuery(BankTransaction.class);

        Root<BankTransaction> root = query.from(BankTransaction.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("personId"), personId));

        if (filterParams.containsKey("transactionTypeId")) {
            predicates.add(criteriaBuilder.equal(root.get("transactionTypeId"), Long.parseLong(filterParams.get("transactionTypeId"))));
        }

        if (filterParams.containsKey("transactionDateFrom") && !filterParams.containsKey("transactionDateTo")) {
            predicates.add(criteriaBuilder.greaterThan(root.get("transactionDate"), Date.valueOf(filterParams.get("transactionDateFrom"))));
        } else if (!filterParams.containsKey("transactionDateFrom") && filterParams.containsKey("transactionDateTo")) {
            predicates.add(criteriaBuilder.lessThan(root.get("transactionDate"), Date.valueOf(filterParams.get("transactionDateTo"))));
        } else if (filterParams.containsKey("transactionDateFrom") && filterParams.containsKey("transactionDateTo")) {
            predicates.add(criteriaBuilder.between(root.get("transactionDate"), Date.valueOf(filterParams.get("transactionDateFrom")), Date.valueOf(filterParams.get("transactionDateTo"))));
        }

        query.distinct(true).where(predicates.toArray(new Predicate[0]));

        List<BankTransaction> resultList;
        if (pageIndex != -1 && pageSize != -1) {
            resultList = entityManager.createQuery(query).setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).getResultList();
            CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
            countCriteria.select(criteriaBuilder.count(root));
            countCriteria.from(query.getResultType());
            countCriteria.where(predicates.toArray(new Predicate[0]));
            Long count = entityManager.createQuery(countCriteria).getSingleResult();
            return new ListResponse(count, resultList);
        } else {
            resultList = entityManager.createQuery(query).getResultList();
            return new ListResponse((long) resultList.size(), resultList);
        }
    }

    @Override
    public byte[] exportMyBankTransactions(Long personId, Map<String, String> filterParams) {
        try {
            ListResponse resultList = this.getMyBankTransactions(personId, filterParams, -1 , -1);

            String[] cols = new String[] {
                    SystemKeys.Exports.MyBankTransactions.Headers.SENDER,
                    SystemKeys.Exports.MyBankTransactions.Headers.SENDER_INVOICE,
                    SystemKeys.Exports.MyBankTransactions.Headers.TARGET,
                    SystemKeys.Exports.MyBankTransactions.Headers.TARGET_INVOICE,
                    SystemKeys.Exports.MyBankTransactions.Headers.TYPE,
                    SystemKeys.Exports.MyBankTransactions.Headers.TRANSACTION_DATE,
                    SystemKeys.Exports.MyBankTransactions.Headers.AMOUNT,
                    SystemKeys.Exports.MyBankTransactions.Headers.OLD_INVOICE_TOTAL,
                    SystemKeys.Exports.MyBankTransactions.Headers.NEW_INVOICE_TOTAL
            };
            Vector<String[]> data = new Vector<>();
            String sheetName = "My bank transactions";

            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

            for (Object row : resultList.getRows()) {
                BankTransaction item = (BankTransaction) row;

                String fromPersonName = item.getFromPerson().getFirstName() + " " + item.getFromPerson().getLastName();
                String toPersonName = item.getToPerson().getFirstName() + " " + item.getToPerson().getLastName();
                data.add(new String[] {
                        fromPersonName,
                        item.getFromInvoiceNumber(),
                        toPersonName,
                        item.getToInvoiceNumber(),
                        item.getTransactionType().getType(),
                        format.format(item.getTransactionDate()),
                        item.getTransactionAmount().toString(),
                        item.getOldInvoiceTotal().toString(),
                        item.getNewInvoiceTotal().toString()
                });
            }

            return XLSUtil.create(data, cols, sheetName);
        } catch (PersonNotFoundException | IOException e) {
            log.error("exportMyBankTransactions", e);
            return null;
        }
    }

}
