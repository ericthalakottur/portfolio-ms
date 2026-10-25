package microservice.portfolio.exception;

public class InvalidKeyOrTokenException extends RuntimeException {
    public InvalidKeyOrTokenException(String message) {
        super(message);
    }
}
