package vn.bachdao.commonservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import vn.bachdao.commonservice.errors.CommonException;
import vn.bachdao.commonservice.errors.ErrorMessage;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        log.error("Unknown internal server error: " + ex.getMessage());
        log.error("Exception class: " + ex.getClass());
        log.error("Exception cause: " + ex.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage("9999", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(value = { CommonException.class })
    public ResponseEntity<ErrorMessage> handleCommonException(CommonException ex) {
        log.error(String.format("Common error: %s %s %s", ex.getCode(), ex.getStatus(), ex.getMessage()));
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorMessage(ex.getCode(), ex.getMessage(), ex.getStatus()));
    }
}
