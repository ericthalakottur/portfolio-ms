package microservice.portfolio.service;

import java.util.Map;

public interface S3Service {

    Map<String, String> getS3PresignedURL(String id, String token);
}
