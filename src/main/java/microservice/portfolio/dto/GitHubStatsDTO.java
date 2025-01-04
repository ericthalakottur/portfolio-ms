package microservice.portfolio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Getter
@Builder
@Jacksonized
public class GitHubStatsDTO {
    private Long id;
    private String name;
    private String description;
    private String htmlUrl;
    private Set<String> languages;
}
