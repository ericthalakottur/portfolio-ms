package microservice.portfolio.api;

import microservice.portfolio.dto.RepositoryDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

@HttpExchange(accept = "application/vnd.github+json")
public interface GitHubAPI {

    @GetExchange("/users/{username}/repos?type=owner&sort=updated&direction=desc&per_page=10")
    List<RepositoryDTO> getListOfUserRepositories(@PathVariable String username);

    @GetExchange("/repos/{username}/{repo}/languages")
    Map<String, Integer> getRespositoryLanguages(@PathVariable String username, @PathVariable String repo);
}
