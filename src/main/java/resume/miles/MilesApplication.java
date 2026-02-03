package resume.miles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;




@SpringBootApplication
@EnableJpaAuditing
public class MilesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MilesApplication.class, args);
	}
	
}
