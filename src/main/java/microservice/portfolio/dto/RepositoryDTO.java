package microservice.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RepositoryDTO(
        Long id,
        String name,
        @JsonProperty("html_url")
        String htmlUrl,
        String description,
        Boolean archived,
        Boolean disabled,
        @JsonProperty("private")
        Boolean isPrivate
) {
}
