package microservice.portfolio.service.impl;

import lombok.extern.slf4j.Slf4j;
import microservice.portfolio.api.GitHubAPI;
import microservice.portfolio.dto.GitHubStatsDTO;
import microservice.portfolio.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class GitHubServiceImpl implements GitHubService {

    private final GitHubAPI gitHubAPI;
    private List<GitHubStatsDTO> gitHubStatsList;

    @Value("${github.username}")
    private String gitHubUsername;

    @Autowired
    public GitHubServiceImpl(GitHubAPI gitHubAPI) {
        this.gitHubAPI = gitHubAPI;
        this.gitHubStatsList = new ArrayList<>();
    }

    @Override
    public Flux<GitHubStatsDTO> getGitHubStats() {
        return Flux.fromIterable(this.gitHubStatsList);
    }

    @Scheduled(fixedRateString = "${github.stats-update-rate:PT24H}")
    private void updateGitHubStats() {
        log.info("Updating GitHub stats");

        this.gitHubStatsList = gitHubAPI.getListOfUserRepositories(gitHubUsername)
                .stream()
                .filter(repositoryDTO -> !gitHubUsername.equals(repositoryDTO.name())
                        && !repositoryDTO.archived()
                        && !repositoryDTO.disabled()
                        && !repositoryDTO.isPrivate()
                )
                .map(repositoryDTO -> {
                    Set<String> repositoryLanguages = gitHubAPI.getRepositoryLanguages(gitHubUsername, repositoryDTO.name())
                            .keySet();

                    return GitHubStatsDTO.builder()
                            .id(repositoryDTO.id())
                            .name(repositoryDTO.name())
                            .description(repositoryDTO.description())
                            .htmlUrl(repositoryDTO.htmlUrl())
                            .languages(repositoryLanguages)
                            .build();
                })
                .toList();

        log.info("Finished updating GitHub stats");
    }
}
