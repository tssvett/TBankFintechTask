package tssvett.dev.translator.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .exceptionName(e.getClass().getSimpleName())
                .exceptionClass(e.getStackTrace()[0].getClassName())
                .exceptionMessage(e.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> "Field '" + fieldError.getField() + "': " + fieldError.getDefaultMessage()).toList())
                .exceptionTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
}
