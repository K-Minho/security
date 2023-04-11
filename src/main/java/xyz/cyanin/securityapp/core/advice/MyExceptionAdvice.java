package xyz.cyanin.securityapp.core.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import xyz.cyanin.securityapp.dto.ResponseDTO;

@RestControllerAdvice
public class MyExceptionAdvice {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> ex(Exception e) {
        ResponseDTO<?> responseDTO = new ResponseDTO<>().fail(500, "error!", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
