package ponomarev.dev.eventmanager.web;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage());
        var errorDto = new ServerErrorDto(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerErrorDto> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Get illegal argument", e);
        var errorDto = new ServerErrorDto(
                "Ошибка в запросе",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        var detailMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var errorDto = new ServerErrorDto(
                "Ошибка валидации",
                detailMessage,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServerErrorDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        var errorDto = new ServerErrorDto(
                "Json invalid format",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(errorDto);
    }
}
