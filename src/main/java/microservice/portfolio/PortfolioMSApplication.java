package microservice.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableR2dbcAuditing
public class PortfolioMSApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PortfolioMSApplication.class, args);
	}

}
