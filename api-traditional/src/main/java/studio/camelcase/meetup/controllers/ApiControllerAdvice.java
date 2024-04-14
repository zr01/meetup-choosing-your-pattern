package studio.camelcase.meetup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import studio.camelcase.meetup.exceptions.PersonControllerException;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(PersonControllerException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ProblemDetail handlePersonControllerException(
        PersonControllerException e
    ) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            e.getMessage()
        );
    }
}
