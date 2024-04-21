package studio.camelcase.meetup.exceptions;

public class ResourceDoesNotExistException extends RuntimeException {

    public ResourceDoesNotExistException(
        String message,
        Throwable cause
    ) {
        super(message, cause);
    }
}
