package ait.cohort34.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends Throwable {
    public UserAlreadyExistsException(String login) {
    }
}
