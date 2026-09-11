package microservice.portfolio.controller;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
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

    private final RateLimiter rateLimiter;
    private final GitHubService gitHubService;

    @Autowired
    public StatsController(RateLimiterRegistry rateLimiterRegistry, GitHubService gitHubService) {
        this.rateLimiter = rateLimiterRegistry.rateLimiter("default");
        this.gitHubService = gitHubService;
    }

    @GetMapping("/github")
    Flux<GitHubStatsDTO> getGithubStats() {
        return gitHubService.getGitHubStats()
                .transformDeferred(RateLimiterOperator.of(rateLimiter));
    }
}
