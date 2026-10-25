package microservice.portfolio.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.UUID;

@DynamoDbBean
public class PublicFileLink {
    private UUID id;
    private String filename;
    private Long count;
    private boolean active;

    @DynamoDbPartitionKey
    public String getId() {
        return this.id.toString();
    }

    @DynamoDbSortKey
    public Long getCount() {
        return this.count;
    }
}
