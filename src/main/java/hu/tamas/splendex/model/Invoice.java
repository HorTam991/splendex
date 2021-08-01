package hu.tamas.splendex.model;

import hu.tamas.splendex.model.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "invoice")
@Getter
@Setter
public class Invoice extends AbstractEntity {

    @Column(name = "person_id")
    private Long personId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    private Person person;

    @Column(name = "actual_invoice_total")
    private Long actualInvoiceTotal;

    @Column(name = "invoice_number")
    private String invoiceNumber;

}
