package poc.ignite.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.domain.Person;
import poc.ignite.utils.DateTimeUtils;

@Slf4j
@Service
public class CacheService {

	@Autowired
	private Ignite ignite;
	private IgniteCache<Integer, Person> personCache;

	public void createCaches() {
		log.debug("createCaches service");

		CacheConfiguration<Integer, Person> personCacheConfig = new CacheConfiguration<>("person-cache");
		personCacheConfig.setIndexedTypes(Integer.class, Person.class);
		personCacheConfig.setCacheMode(CacheMode.PARTITIONED);
		personCacheConfig.setSqlSchema("ip");

		ignite.addCacheConfiguration(personCacheConfig);

		personCache = ignite.getOrCreateCache(personCacheConfig.getName());
	}

	public void loadCaches() throws Exception {
		log.debug("loadCaches service");

		long millis80 = 83403758866254L; // 2020-04-29 15:12:43.0
		int seconds80 = 1272618900; // 2020-04-29 09:15:00.0

		for (int i = 1; i <= 5; i++) {
			Person p;
			if (i % 2 == 0)
				p = new Person(i, "person" + i, i, i,
						Instant.ofEpochMilli(1588173163000L).atZone(ZoneId.systemDefault()).toLocalDate(),
						LocalDateTime.ofEpochSecond(1588173163000L / 1000, 0, ZoneOffset.UTC), new Date(1588173163000L),
						new Date(DateTimeUtils.toMillis(seconds80)), new Timestamp(DateTimeUtils.toMillis(millis80)),
						new Timestamp(DateTimeUtils.toMillisPt(millis80)));
			else
				p = new Person();

			try {
				log.debug("Object size: " + ignite.configuration().getMarshaller().marshal(p).length);
			} catch (IgniteCheckedException e) {
				log.error(e.getMessage(), e);
			}
			personCache.put(i, p);
		}

		personCache.forEach(p -> {
			log.debug("p: " + p);
		});
	}

	public void main() {
		log.debug("main service");

		createCaches();
		try {
			loadCaches();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
