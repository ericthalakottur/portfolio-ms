package microservice.portfolio.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class GitHubStatsDTO {
    private String name;
    private String description;
    private String htmlUrl;
    private Set<String> languages;
}
