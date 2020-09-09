package poc.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.events.Event;
import org.apache.ignite.events.EventType;
import org.apache.ignite.lang.IgnitePredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class IgniteClientApplication {

	@Autowired
	private Ignite ignite;

	public static void main(String[] args) {
		SpringApplication.run(IgniteClientApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ac) {
		return args -> {
			IgnitePredicate<Event> localEvent = event -> {
				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>name: " + event.name());
				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>message: " + event.message());
				return false;
			};

			ignite.events().localListen(localEvent, EventType.EVT_NODE_METRICS_UPDATED);
		};
	}

}
