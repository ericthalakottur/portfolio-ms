package microservice.portfolio.config;

import microservice.portfolio.api.GitHubAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ApiConfig {

    @Value("${github.base-url}")
    private String gitHubBaseUrl;

    @Value("${github.api-version}")
    private String gitHubApiVersion;

    @Bean
    public GitHubAPI gitHubService() {
        RestClient restClient = RestClient.builder()
                .baseUrl(gitHubBaseUrl)
                .defaultHeader("X-GitHub-Api-Version", gitHubApiVersion)
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(GitHubAPI.class);
    }
}
