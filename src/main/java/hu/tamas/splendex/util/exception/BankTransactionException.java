package hu.tamas.splendex.util.exception;

public class BankTransactionException extends Exception {

    private static final long serialVersionUID = 5779750082416330178L;

    public BankTransactionException(String message) {
        super(message);
    }

}
