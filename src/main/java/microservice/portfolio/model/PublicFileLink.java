package microservice.portfolio.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.UUID;

@Data
@DynamoDbBean
public class PublicFileLink {
    private UUID id;
    private String filename;
    private String token;
    private boolean active;

    @DynamoDbPartitionKey
    public String getId() {
        return this.id.toString();
    }
}
