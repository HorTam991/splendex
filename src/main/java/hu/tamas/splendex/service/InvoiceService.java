package hu.tamas.splendex.service;

import hu.tamas.splendex.model.Invoice;

public interface InvoiceService {

    Invoice save(Invoice invoice);

    Invoice update(Invoice invoice);

    Invoice findByInvoiceNumber(String invoiceNumber);

    void deleteById(Long id);

    Invoice findByPersonId(Long personId);

}
