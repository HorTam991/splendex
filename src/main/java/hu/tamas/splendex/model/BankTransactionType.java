package hu.tamas.splendex.model;

import hu.tamas.splendex.model.core.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bank_transaction_type")
@Getter
@Setter
public class BankTransactionType extends AbstractEntity {

    @Column(name = "type")
    private String type;

}
