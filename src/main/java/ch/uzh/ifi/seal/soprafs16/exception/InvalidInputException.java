package ch.uzh.ifi.seal.soprafs16.exception;

/**
 * Exception is thrown, when the given input attributes for a colt express service is not valid.
 *
 * Created by soyabeen on 29.03.16.
 */
public class InvalidInputException extends IllegalArgumentException {

    public InvalidInputException() {
    }

    public InvalidInputException(String s) {
        super(s);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
