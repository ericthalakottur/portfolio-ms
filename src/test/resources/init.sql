CREATE TABLE IF NOT EXISTS portfolio.github_stats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    github_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    html_url VARCHAR(100) NOT NULL,
    languages VARCHAR(500),
    last_modified_date DATETIME
);