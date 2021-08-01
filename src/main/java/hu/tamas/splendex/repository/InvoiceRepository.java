package hu.tamas.splendex.repository;

import hu.tamas.splendex.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Invoice findByInvoiceNumber(String invoiceNumber);

    Invoice findByPersonId(Long personId);

}
