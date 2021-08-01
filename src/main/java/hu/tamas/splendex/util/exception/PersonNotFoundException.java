package hu.tamas.splendex.util.exception;

public class PersonNotFoundException extends Exception {

    private static final long serialVersionUID = 6240577387856263196L;

    public PersonNotFoundException(String message) {
        super(message);
    }

}
