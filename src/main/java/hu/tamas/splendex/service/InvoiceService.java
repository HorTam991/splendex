package hu.tamas.splendex.service;

import hu.tamas.splendex.model.Invoice;
import hu.tamas.splendex.util.exception.AlreadyExistsInvoiceException;

public interface InvoiceService {

    Invoice save(Invoice invoice) throws AlreadyExistsInvoiceException;

    Invoice update(Invoice invoice);

    Invoice findByInvoiceNumber(String invoiceNumber);

    void deleteById(Long id);

    Invoice findByPersonId(Long personId);

}
