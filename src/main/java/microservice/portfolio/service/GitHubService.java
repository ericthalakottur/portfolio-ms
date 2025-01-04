package microservice.portfolio.service;

import microservice.portfolio.dto.GitHubStatsDTO;
import reactor.core.publisher.Flux;

public interface GitHubService {

    Flux<GitHubStatsDTO> getGitHubStats();
}
