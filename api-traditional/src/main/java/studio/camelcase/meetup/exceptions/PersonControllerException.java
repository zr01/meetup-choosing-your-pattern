package studio.camelcase.meetup.exceptions;

public class PersonControllerException extends RuntimeException {

    public PersonControllerException(
        String message,
        Throwable cause
    ) {
        super(message, cause);
    }

    public PersonControllerException(String message) {
        super(message);
    }
}
