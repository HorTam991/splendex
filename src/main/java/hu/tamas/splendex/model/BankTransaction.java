package hu.tamas.splendex.model;

import hu.tamas.splendex.model.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "bank_transaction")
@Getter
@Setter
public class BankTransaction extends AbstractEntity {

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "from_person_id")
    private Long fromPersonId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "from_person_id", insertable = false, updatable = false)
    private PersonView fromPerson;

    @Column(name = "from_invoice_number")
    private String fromInvoiceNumber;

    @Column(name = "to_person_id")
    private Long toPersonId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "to_person_id", insertable = false, updatable = false)
    private PersonView toPerson;

    @Column(name = "to_invoice_number")
    private String toInvoiceNumber;

    @Column(name = "transaction_type_id")
    private Long transactionTypeId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_type_id", insertable = false, updatable = false)
    private BankTransactionType transactionType;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "transaction_amount")
    private Long transactionAmount;

    @Column(name = "old_invoice_total")
    private Long oldInvoiceTotal;

    @Column(name = "new_invoice_total")
    private Long newInvoiceTotal;

}
