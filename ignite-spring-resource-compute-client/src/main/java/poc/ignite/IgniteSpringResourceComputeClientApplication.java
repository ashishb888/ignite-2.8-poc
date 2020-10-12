package poc.ignite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import poc.ignite.service.ComputeService;

@SpringBootApplication
public class IgniteSpringResourceComputeClientApplication {

	@Autowired
	private ComputeService cs;

	public static void main(String[] args) {
		SpringApplication.run(IgniteSpringResourceComputeClientApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ac) {
		return args -> {
			cs.main();
		};
	}
}
