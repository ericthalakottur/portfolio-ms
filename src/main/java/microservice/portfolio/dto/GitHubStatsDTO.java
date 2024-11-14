package microservice.portfolio.dto;

import lombok.Data;

import java.util.Set;

@Data
public class GitHubStatsDTO {
    private String name;
    private String description;
    private String htmlUrl;
    private Set<String> languages;
}
