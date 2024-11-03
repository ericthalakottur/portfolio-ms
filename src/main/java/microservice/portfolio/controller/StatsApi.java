package microservice.portfolio.controller;

import microservice.portfolio.dto.GitHubStatsDTO;
import microservice.portfolio.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsApi {

    private final GitHubService gitHubService;

    @Autowired
    public StatsApi(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/github")
    List<GitHubStatsDTO> getGithubStats() {
        return gitHubService.getGitHubStats();
    }
}
