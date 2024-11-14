package microservice.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryDTO {
    private String name;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String description;
    private Boolean archived;
    private Boolean disabled;
    @JsonProperty("private")
    private Boolean isPrivate;
}
