package microservice.portfolio.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.portfolio.api.GitHubAPI;
import microservice.portfolio.dto.GitHubStatsDTO;
import microservice.portfolio.dto.RepositoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubAPI gitHubAPI;

    @Value("${github.username}")
    private String gitHubUsername;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws InterruptedException {
        var repositoryList = List.of(
                RepositoryDTO.builder()
                        .name("test-repository-1")
                        .htmlUrl("http://localhost")
                        .description("Test Repository 1")
                        .archived(false)
                        .disabled(false)
                        .isPrivate(false)
                        .build(),
                RepositoryDTO.builder()
                        .name(gitHubUsername)
                        .htmlUrl("http://localhost")
                        .description(gitHubUsername + " Repository")
                        .archived(false)
                        .disabled(false)
                        .isPrivate(false)
                        .build(),
                RepositoryDTO.builder()
                        .name("test-repository-2")
                        .htmlUrl("http://localhost")
                        .description("Test Repository 2")
                        .archived(true)
                        .disabled(false)
                        .isPrivate(false)
                        .build(),
                RepositoryDTO.builder()
                        .name("test-repository-3")
                        .htmlUrl("http://localhost")
                        .description("Test Repository 3")
                        .archived(false)
                        .disabled(true)
                        .isPrivate(false)
                        .build(),
                RepositoryDTO.builder()
                        .name("private-repository")
                        .htmlUrl("http://localhost")
                        .description("Private Repository")
                        .archived(false)
                        .disabled(false)
                        .isPrivate(true)
                        .build()
        );
        Mockito.doReturn(repositoryList)
                .when(gitHubAPI).getListOfUserRepositories(Mockito.anyString());
        Mockito.doReturn(Map.ofEntries(Map.entry("Java", 100), Map.entry("Python", 50)))
                .when(gitHubAPI).getRespositoryLanguages(Mockito.anyString(), Mockito.anyString());

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    void testGetGitHubStats() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/stats/github"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<GitHubStatsDTO> gitHubStatsDTOList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

        assertThat(gitHubStatsDTOList)
                .as("Check length of list").hasSize(1)
                .allSatisfy(repository -> {
                    assertThat(repository.getName())
                           .isNotBlank()
                           .isNotEqualTo(gitHubUsername);
                    assertThat(repository.getDescription())
                           .isNotBlank();
                    assertThat(repository.getHtmlUrl())
                            .isNotBlank();
                    assertThat(repository.getLanguages())
                            .isNotNull()
                            .isNotEmpty()
                            .isInstanceOf(HashSet.class);
                });
    }
}