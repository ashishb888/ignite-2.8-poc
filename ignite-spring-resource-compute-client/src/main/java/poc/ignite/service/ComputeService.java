package poc.ignite.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.ignite.Ignite;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.SpringApplicationContextResource;
import org.apache.ignite.resources.SpringResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ComputeService {

	@Autowired
	private Ignite ignite;

	private void affinityCall() {
		log.debug("affinityCall service");

		String cacheName = "person-cache";

		List<Integer> affKeys = IntStream.range(1, 51).boxed().collect(Collectors.toList());
		// IntStream.iterate(1, i -> i +
		// 1).limit(50).boxed().collect(Collectors.toList());
		Thread t = new Thread(() -> {
			while (true) {
				affKeys.forEach(affKey -> {
					long records = ignite.compute().affinityCall(cacheName, affKey, new ICCall(affKey, cacheName));
					log.debug("records: " + records);
				});

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			}
		});

		t.setName("affinity-call");
		t.start();
	}

	@Slf4j
	static class ICCall implements IgniteCallable<Long> {
		private static final long serialVersionUID = 4278959731940740185L;

		@IgniteInstanceResource
		private Ignite ignite;
		@SpringResource(resourceName = "testService")
		private TestService ts;
		@SpringApplicationContextResource
		private ApplicationContext ac;

		private int affKey;
		private String cacheName;

		ICCall(int affKey, String cacheName) {
			this.affKey = affKey;
			this.cacheName = cacheName;
		}

		@Override
		public Long call() {
			log.debug("ignite: " + ignite.cacheNames());
			log.debug("affKey: " + affKey + ", cacheName: " + cacheName);
			log.debug("beans: " + Arrays.toString(ac.getBeanDefinitionNames()));

			return ts.call(affKey, cacheName);
		}
	}

	public void main() {
		log.debug("main service");

		affinityCall();
	}
}
