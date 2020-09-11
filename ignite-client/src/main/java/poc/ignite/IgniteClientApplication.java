package poc.ignite;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.config.BeansConfig;
import poc.ignite.domain.IgniteRestResponse;

@Slf4j
@SpringBootApplication
public class IgniteClientApplication {

//	@Autowired
//	private Ignite ignite;

	@Autowired
	private BeansConfig bc;

	public static void main(String[] args) {
		SpringApplication.run(IgniteClientApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ac) {
		return args -> {
			log.debug("commandLineRunner bean");

//			IgnitePredicate<Event> localEvent = event -> {
//				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>name: " + event.name());
//				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>message: " + event.message());
//				return false;
//			};
//
//			ignite.events().localListen(localEvent, EventType.EVT_NODE_METRICS_UPDATED);

			// bc.ignite();

			// isClusterUp();

			connectToCluster();

			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>END");
		};
	}

	private void connectToCluster() {
		log.debug("connectToCluster service");

		Thread t = new Thread(() -> {
			boolean isClusterUp = false;

			while (!isClusterUp) {
				log.debug("While loop");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}

				isClusterUp = isClusterUp();

				if (isClusterUp) {
					bc.ignite();
				}
			}
		});

		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.schedule(t, 10000, TimeUnit.MILLISECONDS);
	}

	private boolean isClusterUp() {
		log.debug("isClusterUp service");

		RestTemplate restTemplate = new RestTemplate();
		try {
			IgniteRestResponse irr = restTemplate.getForObject("http://localhost:8080/ignite?cmd=version",
					IgniteRestResponse.class);
			log.debug("irr: " + irr);

			if (irr.getSuccessStatus() == 0)
				return true;

			return false;

		} catch (RestClientException e) {
			// log.error(e.getMessage(), e);
			log.error(e.getMessage());
			return false;
		}
	}

}
