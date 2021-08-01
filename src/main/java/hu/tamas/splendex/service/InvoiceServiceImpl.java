package hu.tamas.splendex.service;

import hu.tamas.splendex.model.Invoice;
import hu.tamas.splendex.repository.InvoiceRepository;
import hu.tamas.splendex.util.SystemKeys;
import hu.tamas.splendex.util.exception.AlreadyExistsInvoiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice save(Invoice invoice) throws AlreadyExistsInvoiceException {
        Invoice dbData = this.invoiceRepository.findByInvoiceNumber(invoice.getInvoiceNumber());
        if (dbData != null) {
            throw new AlreadyExistsInvoiceException(SystemKeys.ExceptionTexts.INVOICE_ALREADY_EXISTS);
        }
        Invoice dbData2 = this.invoiceRepository.findByPersonId(invoice.getPersonId());
        if (dbData2 != null) {
            throw new AlreadyExistsInvoiceException(SystemKeys.ExceptionTexts.CUSTOMER_ALREADY_HAS_INVOICE);
        }
        return this.invoiceRepository.save(invoice);
    }

    @Override
    public Invoice update(Invoice invoice) {
        return this.invoiceRepository.save(invoice);
    }

    @Override
    public Invoice findByInvoiceNumber(String invoiceNumber) {
        return this.invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    @Override
    public void deleteById(Long id) {
        this.invoiceRepository.deleteById(id);
    }

    @Override
    public Invoice findByPersonId(Long personId) {
        return this.invoiceRepository.findByPersonId(personId);
    }

}
