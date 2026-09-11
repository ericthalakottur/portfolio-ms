package microservice.portfolio.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Mono<ResponseEntity<Map<String, String>>> handleRequestNotPermitted(RequestNotPermitted requestNotPermitted) {
        Map<String, String> errorBody = Map.of(
                "timestamp", Instant.now().toString(),
                "message", "Rate limit exceeded"
        );
        return Mono.just(
                ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(errorBody)
        );
    }
}
