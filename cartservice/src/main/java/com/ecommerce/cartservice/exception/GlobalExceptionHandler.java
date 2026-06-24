package com.ecommerce.cartservice.exception;

import com.ecommerce.cartservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<ErrorResponse> productNotFoundException(ProductNotFound ex){

        ErrorResponse errorResponse= new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<> (errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> inSufficientStock(InsufficientStockException ex){
        ErrorResponse errorResponse= new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> cartNotFound(CartNotFoundException ex){
        ErrorResponse errorResponse= new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return  new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
}
