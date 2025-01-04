package microservice.portfolio.config;

import microservice.portfolio.api.AtCoderAPI;
import microservice.portfolio.api.GitHubAPI;
import microservice.portfolio.api.HackTheBoxAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ApiConfig {

    private final String gitHubBaseUrl;
    private final String gitHubApiVersion;
    private final String atCoderBaseUrl;
    private final String hackTheBoxBaseUrl;
    private final String hackTheBoxApiToken;

    public ApiConfig(@Value("${github.base-url}") String gitHubBaseUrl,
                     @Value("${github.api-version}") String gitHubApiVersion,
                     @Value("${atcoder.base-url}") String atCoderBaseUrl,
                     @Value("${hackthebox.base-url}") String hackTheBoxBaseUrl,
                     @Value("${hackthebox.api-token}") String hackTheBoxApiToken) {
        this.gitHubBaseUrl = gitHubBaseUrl;
        this.gitHubApiVersion = gitHubApiVersion;
        this.atCoderBaseUrl = atCoderBaseUrl;
        this.hackTheBoxBaseUrl = hackTheBoxBaseUrl;
        this.hackTheBoxApiToken = hackTheBoxApiToken;
    }

    @Bean
    public GitHubAPI gitHubAPI() {
        RestClient restClient = RestClient.builder()
                .baseUrl(this.gitHubBaseUrl)
                .defaultHeader("X-GitHub-Api-Version", this.gitHubApiVersion)
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(GitHubAPI.class);
    }

    @Bean
    public AtCoderAPI atCoderAPI() {
        RestClient restClient = RestClient.builder()
                .baseUrl(this.atCoderBaseUrl)
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(AtCoderAPI.class);
    }

    @Bean
    public HackTheBoxAPI hackTheBoxAPI() {
        RestClient restClient = RestClient.builder()
                .baseUrl(this.hackTheBoxBaseUrl)
                .defaultHeader("Authorization", "Bearer: " + this.hackTheBoxApiToken)
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(HackTheBoxAPI.class);
    }
}
