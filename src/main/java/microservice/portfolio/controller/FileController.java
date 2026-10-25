package microservice.portfolio.controller;

import lombok.RequiredArgsConstructor;
import microservice.portfolio.service.S3Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final S3Service s3Service;

    @GetMapping(path = "/files/{key}", version = "1")
    public Map<String, String> getS3PresignedURL(@PathVariable("key") String key, @RequestParam("token") String token) {
        return s3Service.getS3PresignedURL(key, token);
    }
}
