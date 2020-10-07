package poc.ignite.service;

import java.util.List;

import javax.cache.Cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.domain.Person;
import poc.ignite.domain.PersonKey;

@Slf4j
@Service
public class CacheService {

	@Autowired
	private Ignite ignite;
	private IgniteCache<PersonKey, Person> personCache;

	private void createCaches() {
		log.debug("createCaches service");

		CacheConfiguration<Integer, Person> personCacheConfig = new CacheConfiguration<>("person-cache");
		personCacheConfig.setIndexedTypes(PersonKey.class, Person.class);
		personCacheConfig.setCacheMode(CacheMode.PARTITIONED);
		personCacheConfig.setSqlSchema("ip");

		ignite.addCacheConfiguration(personCacheConfig);

		personCache = ignite.getOrCreateCache(personCacheConfig.getName());
	}

	private void loadCaches() {
		log.debug("loadCaches service");

		for (int i = 1; i <= 5; i++) {
			personCache.put(new PersonKey(i, "p" + i), new Person(i, "p" + i, i, i));
		}

		personCache.forEach(p -> {
			log.debug("p: " + p);
		});
	}

	private void cacheKeys() {
		log.debug("cacheKeys service");

		SqlFieldsQuery allKeys = new SqlFieldsQuery("select _key from Person");
		try (QueryCursor<List<?>> cursor = personCache.query(allKeys)) {
			cursor.forEach(r -> {
				log.debug("SFQ key: " + r.get(0));
			});
		}

		ScanQuery<Integer, Person> sq = new ScanQuery<>(null);
		try (QueryCursor<Cache.Entry<Integer, Person>> cursor = personCache.query(sq)) {
			cursor.forEach(r -> {
				log.debug("SQ key: " + r.getKey());
			});
		}

	}

	public void main() {
		log.debug("main service");

		createCaches();
		loadCaches();
		cacheKeys();
	}
}
