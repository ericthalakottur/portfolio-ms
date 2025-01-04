package microservice.portfolio.repository;

import microservice.portfolio.entity.GitHubStatsEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface GitHubStatsRepository extends ReactiveCrudRepository<GitHubStatsEntity, Long> {

    @Query("SELECT * FROM portfolio.github_stats WHERE github_id = :gitHubId")
    Mono<GitHubStatsEntity> findByGitHubId(Long gitHubId);
}
