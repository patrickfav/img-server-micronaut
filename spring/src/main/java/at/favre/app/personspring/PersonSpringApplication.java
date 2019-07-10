package at.favre.app.personspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("at.favre.app")
public class PersonSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonSpringApplication.class, args);
	}

}
