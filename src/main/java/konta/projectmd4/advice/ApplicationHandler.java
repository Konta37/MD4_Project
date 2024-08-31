package konta.projectmd4.advice;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.resp.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApplicationHandler {

    public ApplicationHandler(){

    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e)
    {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return new ResponseEntity<>(body, e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Map<String,String>>> handleArgumentNotValid(MethodArgumentNotValidException ex){
        Map<String,String> map = new HashMap<>();
        //ex.getFieldErrors().stream().forEach(err -> map.put(err.getField(),err.getDefaultMessage()));
        for(int i=0;i<ex.getAllErrors().size();i++){
            map.put("error: "+(i+1),ex.getAllErrors().get(i).getDefaultMessage());
        }
        return new ResponseEntity<>(new ErrorResponse<>("Error",map, HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<ErrorResponse<String>> handleNoSuchElement(NoSuchElementException ex){
//        return new ResponseEntity<>(new ErrorResponse<>("Error",ex.getMessage(),HttpStatus.NOT_FOUND),HttpStatus.NOT_FOUND);
//    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse<String>> handleNoResourceFound(NoResourceFoundException ex){
        return new ResponseEntity<>(new ErrorResponse<>("Error",ex.getMessage(),HttpStatus.NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        return new ResponseEntity<>(new ErrorResponse<>("Error",ex.getMessage(),HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErrorResponse<String>> handleHttpMessageConversionException(HttpMessageConversionException ex){
        return new ResponseEntity<>(new ErrorResponse<>("Error",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
