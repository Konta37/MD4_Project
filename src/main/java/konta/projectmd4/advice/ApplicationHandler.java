package konta.projectmd4.advice;

import konta.projectmd4.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e)
    {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}
