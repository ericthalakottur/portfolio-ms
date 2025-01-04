package microservice.portfolio.controller;

import microservice.portfolio.dto.GitHubStatsDTO;
import microservice.portfolio.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final GitHubService gitHubService;

    @Autowired
    public StatsController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/github")
    Flux<GitHubStatsDTO> getGithubStats() {
        return gitHubService.getGitHubStats();
    }
}
