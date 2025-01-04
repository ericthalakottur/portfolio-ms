package microservice.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class RepositoryDTO {
    private Long id;
    private String name;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String description;
    private Boolean archived;
    private Boolean disabled;
    @JsonProperty("private")
    private Boolean isPrivate;
}
