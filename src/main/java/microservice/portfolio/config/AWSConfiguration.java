package microservice.portfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AWSConfiguration {
    private static final Region AWS_REGION = Region.AF_SOUTH_1;

    private final String s3AccessKey;
    private final String s3SecretKey;
    private final String dynamodbAccessKey;
    private final String dynamodbSecretKey;
    private final String dynamodbEndpoint;

    AWSConfiguration(
            @Value("{aws.s3.access-key}") String s3AccessKey,
            @Value("{aws.s3.secret-key}") String s3SecretKey,
            @Value("{aws.dynamodb.access-key}") String dynamodbAccessKey,
            @Value("{aws.dynamodb.secret-key}") String dynamodbSecretKey,
            @Value("{aws.dynamodb.endpoint}") String dynamodbEndpoint) {
        this.s3AccessKey = s3AccessKey;
        this.s3SecretKey = s3SecretKey;
        this.dynamodbAccessKey = dynamodbAccessKey;
        this.dynamodbSecretKey = dynamodbSecretKey;
        this.dynamodbEndpoint = dynamodbEndpoint;
    }

    private AwsCredentialsProvider s3CredentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(s3AccessKey, s3SecretKey)
        );
    }

    private AwsCredentialsProvider dynamodbCredentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(dynamodbAccessKey, dynamodbSecretKey)
        );
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(AWS_REGION)
                .credentialsProvider(s3CredentialsProvider())
                .build();
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(AWS_REGION)
                .credentialsProvider(dynamodbCredentialsProvider())
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(final DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}
