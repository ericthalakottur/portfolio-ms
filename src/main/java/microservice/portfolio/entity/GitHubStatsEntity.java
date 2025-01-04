package microservice.portfolio.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "github_stats")
public class GitHubStatsEntity {
    @Id
    @Column("id")
    private Long id;
    @Column("github_id")
    private Long githubId;
    @Column("name")
    private String name;
    @Column("description")
    private String description;
    @Column("html_url")
    private String htmlUrl;
    @Column("languages")
    private String languages;
    @LastModifiedDate
    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;
}
