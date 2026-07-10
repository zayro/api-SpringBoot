package api.rest.artefacto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"api.rest.artefacto", "com.rest.api"})
public class ArtefactoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtefactoApplication.class, args);
	}

}
