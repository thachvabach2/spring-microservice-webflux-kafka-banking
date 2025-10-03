package vn.bachdao.commonservice.errors;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommonException extends RuntimeException {

    private final String code;
    private final String message;
    private final HttpStatus status;

    public CommonException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
