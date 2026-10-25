package microservice.portfolio.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.portfolio.exception.InvalidKeyOrTokenException;
import microservice.portfolio.exception.NoSuchFileException;
import microservice.portfolio.model.PublicFileLink;
import microservice.portfolio.service.S3Service;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private static final String BUCKET_NAME = "test";
    private static final String TABLE_NAME = "test";

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private boolean isObjectPresent(final String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException noSuchKeyException) {
            log.error("S3: Key does not exist");
            return false;
        } catch (RuntimeException runtimeException) {
            log.error("S3: {}", runtimeException.getMessage());
            return false;
        }
    }

    private boolean isValidToken(final String key, final String token) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(key));

        Expression expression = Expression.builder()
                .expression("active = true")
                .build();

        QueryEnhancedRequest queryEnhancedRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .filterExpression(expression)
                .build();

        DynamoDbTable<PublicFileLink> dynamoDbTable = this.dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(PublicFileLink.class));
        Optional<PublicFileLink> publicFileLinkOptional = dynamoDbTable.query(queryEnhancedRequest).items().stream().findFirst();
        return publicFileLinkOptional.isPresent();
    }

    public Map<String, String> getS3PresignedURL(final String key, final String token) {
        if (!isValidToken(key, token)) {
            throw new InvalidKeyOrTokenException("Invalid key or token");
        }

        if (!isObjectPresent(key)) {
            throw new NoSuchFileException("Invalid key or token");
        }

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(objectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return Map.of("url", presignedRequest.url().toString());
    }
}
