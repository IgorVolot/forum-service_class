package ait.cohort34.accounting.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserAlreadyExistsException extends Throwable {
    public UserAlreadyExistsException(String login) {
    }
}
