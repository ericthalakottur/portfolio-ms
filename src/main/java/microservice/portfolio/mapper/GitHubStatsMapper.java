package microservice.portfolio.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import microservice.portfolio.dto.GitHubStatsDTO;
import microservice.portfolio.dto.RepositoryDTO;
import microservice.portfolio.entity.GitHubStatsEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class GitHubStatsMapper {

    private final ObjectMapper objectMapper;

    public GitHubStatsMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public GitHubStatsDTO toGitHubStatsDTO(final GitHubStatsEntity gitHubStatsEntity) {
        Set<String> languages = null;
        try {
            languages = this.objectMapper.readValue(gitHubStatsEntity.getLanguages(), new TypeReference<>() {});
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Unable to serialize 'languages' for GitHubStatsEntity id: {}, with error: {}", gitHubStatsEntity.getId(), jsonProcessingException.getMessage());
        }

        return GitHubStatsDTO.builder()
                .id(gitHubStatsEntity.getGithubId())
                .name(gitHubStatsEntity.getName())
                .description(gitHubStatsEntity.getDescription())
                .htmlUrl(gitHubStatsEntity.getHtmlUrl())
                .languages(languages)
                .build();
    }

    public GitHubStatsEntity.GitHubStatsEntityBuilder toGitHubStatsEntityBuilder(RepositoryDTO repositoryDTO, Map<String, Integer> repositoryLanguages) {
        String languages = null;
        try {
            languages = this.objectMapper.writeValueAsString(repositoryLanguages.keySet());
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Unable to serialize 'languages' for Repository id: {}, with error: {}", repositoryDTO.getId(), jsonProcessingException.getMessage());
        }
        return GitHubStatsEntity.builder()
                .githubId(repositoryDTO.getId())
                .name(repositoryDTO.getName())
                .description(repositoryDTO.getDescription())
                .htmlUrl(repositoryDTO.getHtmlUrl())
                .languages(languages);
    }
}
