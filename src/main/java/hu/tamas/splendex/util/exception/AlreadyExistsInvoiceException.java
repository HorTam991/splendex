package hu.tamas.splendex.util.exception;

public class AlreadyExistsInvoiceException extends Exception {

    private static final long serialVersionUID = -6847820383116861955L;

    public AlreadyExistsInvoiceException(String message) {
        super(message);
    }
}
