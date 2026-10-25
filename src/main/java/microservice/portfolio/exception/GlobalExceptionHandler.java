package microservice.portfolio.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleRequestNotPermitted(RequestNotPermitted requestNotPermitted) {
        Map<String, String> errorBody = Map.of(
                "timestamp", Instant.now().toString(),
                "message", "Rate limit exceeded"
        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(errorBody);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleInvalidKeyOrTokenException(InvalidKeyOrTokenException invalidKeyOrTokenException) {
        log.error(invalidKeyOrTokenException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", invalidKeyOrTokenException.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNoSuchFileException(NoSuchFileException noSuchFileException) {
        log.error(noSuchFileException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", noSuchFileException.getMessage()));
    }
}
