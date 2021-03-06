package poc.ignite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.service.CacheService;
import poc.ignite.service.ContinuousQueriesService;

@Slf4j
@SpringBootApplication
public class IgniteDeleteQueryApplication {

	@Autowired
	private CacheService cs;
	@Autowired
	private ContinuousQueriesService cqs;

	public static void main(String[] args) {
		SpringApplication.run(IgniteDeleteQueryApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ac) {
		return args -> {
			log.debug("commandLineRunner service");
			cqs.main();
			cs.main();
		};
	}

	private void deleteCQ() {

	}
}
