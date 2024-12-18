package microservice.portfolio.service.impl;

import microservice.portfolio.api.GitHubAPI;
import microservice.portfolio.dto.GitHubStatsDTO;
import microservice.portfolio.dto.RepositoryDTO;
import microservice.portfolio.service.GitHubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class GitHubServiceImpl implements GitHubService {

    private final GitHubAPI gitHubAPI;

    private List<GitHubStatsDTO> gitHubStatsDTOList;

    @Value("${github.username}")
    private String gitHubUsername;

    @Autowired
    public GitHubServiceImpl(GitHubAPI gitHubAPI) {
        this.gitHubAPI = gitHubAPI;
        this.gitHubStatsDTOList = Collections.emptyList();
    }

    @Override
    public List<GitHubStatsDTO> getGitHubStats() {
        return this.gitHubStatsDTOList;
    }

    @Scheduled(fixedRateString = "${github.stats-update-rate:PT24H}")
    private void updateGitHubStats() {
        log.info("Updating GitHub stats");

        List<RepositoryDTO> repositoryDTOList = gitHubAPI.getListOfUserRepositories(gitHubUsername).stream()
                .filter(repositoryDTO -> !gitHubUsername.equals(repositoryDTO.getName())
                        && !repositoryDTO.getArchived()
                        && !repositoryDTO.getDisabled()
                        && !repositoryDTO.getIsPrivate()
                )
                .toList();

        this.gitHubStatsDTOList = repositoryDTOList.stream()
                .map(repositoryDTO -> {
                        var repositoryLanguages = gitHubAPI.getRespositoryLanguages(gitHubUsername, repositoryDTO.getName());
                        var gitHubStatsDTO = new GitHubStatsDTO();
                        gitHubStatsDTO.setName(repositoryDTO.getName());
                        gitHubStatsDTO.setDescription(repositoryDTO.getDescription());
                        gitHubStatsDTO.setHtmlUrl(repositoryDTO.getHtmlUrl());
                        gitHubStatsDTO.setLanguages(repositoryLanguages.keySet());

                        return gitHubStatsDTO;
                })
                .toList();

        log.info("Finished updating GitHub stats");
    }
}
