package microservice.portfolio.service;

import microservice.portfolio.dto.GitHubStatsDTO;

import java.util.List;

public interface GitHubService {

    List<GitHubStatsDTO> getGitHubStats();
}
