package microservice.portfolio.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.portfolio.api.GitHubAPI;
import microservice.portfolio.dto.GitHubStatsDTO;
import microservice.portfolio.entity.GitHubStatsEntity;
import microservice.portfolio.mapper.GitHubStatsMapper;
import microservice.portfolio.repository.GitHubStatsRepository;
import microservice.portfolio.service.GitHubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class GitHubServiceImpl implements GitHubService {

    private final GitHubAPI gitHubAPI;
    private final GitHubStatsRepository gitHubStatsRepository;
    private final GitHubStatsMapper gitHubStatsMapper;
    private final ObjectMapper objectMapper;

    @Value("${github.username}")
    private String gitHubUsername;

    @Autowired
    public GitHubServiceImpl(GitHubAPI gitHubAPI, GitHubStatsRepository gitHubStatsRepository, GitHubStatsMapper gitHubStatsMapper) {
        this.gitHubAPI = gitHubAPI;
        this.gitHubStatsRepository = gitHubStatsRepository;
        this.gitHubStatsMapper = gitHubStatsMapper;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Flux<GitHubStatsDTO> getGitHubStats() {
        return this.gitHubStatsRepository.findAll()
                .map(this.gitHubStatsMapper::toGitHubStatsDTO);
    }

    @Scheduled(fixedRateString = "${github.stats-update-rate:PT24H}")
    private void updateGitHubStats() {
        log.info("Updating GitHub stats");

        List<GitHubStatsEntity> gitHubStatsEntityList = gitHubAPI.getListOfUserRepositories(gitHubUsername)
                .stream()
                .filter(repositoryDTO -> !gitHubUsername.equals(repositoryDTO.getName())
                        && !repositoryDTO.getArchived()
                        && !repositoryDTO.getDisabled()
                        && !repositoryDTO.getIsPrivate()
                )
                .map(repositoryDTO -> {
                    var repositoryLanguages = gitHubAPI.getRespositoryLanguages(gitHubUsername, repositoryDTO.getName());
                    var gitHubStatsEntity = this.gitHubStatsMapper.toGitHubStatsEntityBuilder(repositoryDTO, repositoryLanguages);

                    var gitHubStatsEntityOptional = gitHubStatsRepository.findByGitHubId(repositoryDTO.getId()).blockOptional();
                    gitHubStatsEntityOptional.ifPresent(entity -> gitHubStatsEntity.id(entity.getId()));

                    return gitHubStatsEntity.build();
                })
                .toList();

        try {
            this.gitHubStatsRepository.saveAll(gitHubStatsEntityList).blockLast();
        } catch (Exception e) {
            log.error("Unable to save data to Database: {}", e.getMessage());
        }

        log.info("Finished updating GitHub stats");
    }
}
