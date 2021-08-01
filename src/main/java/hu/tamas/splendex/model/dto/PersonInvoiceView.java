package hu.tamas.splendex.model.dto;

import hu.tamas.splendex.model.Invoice;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class PersonInvoiceView {

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String placeOfBirth;

    private String motherName;

    private Long actualInvoiceTotal;

    private String invoiceNumber;

    public PersonInvoiceView(Invoice invoice) {
        this.firstName = invoice.getPerson().getFirstName();
        this.lastName = invoice.getPerson().getLastName();
        this.dateOfBirth = invoice.getPerson().getDateOfBirth();
        this.placeOfBirth = invoice.getPerson().getPlaceOfBirth();
        this.motherName = invoice.getPerson().getMotherName();
        this.actualInvoiceTotal = invoice.getActualInvoiceTotal();
        this.invoiceNumber = invoice.getInvoiceNumber();
    }

}
