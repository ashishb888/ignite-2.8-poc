package poc.ignite.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.domain.CacheEntryUpdatedListenerImpl;
import poc.ignite.domain.FactoryImpl;
import poc.ignite.domain.Person;
import poc.ignite.domain.PersonKey;
import poc.ignite.domain.PersonStatus;

@Service
@Slf4j
public class ContinuousQueriesService {

	@Autowired
	private Ignite ignite;

	private void start() {
		log.debug("start service");

		IgniteCache<Integer, PersonStatus> personStatusCache = ignite.cache("person-status-cache");

		ContinuousQuery<Integer, PersonStatus> cQuery = new ContinuousQuery<>();

		cQuery.setLocalListener(new CacheEntryUpdatedListenerImpl());
		cQuery.setRemoteFilterFactory(new FactoryImpl());

		personStatusCache.query(cQuery);
	}

	private void createCaches() {
		log.debug("createCaches service");

		CacheConfiguration<Integer, Person> personCacheConfig = new CacheConfiguration<>("person-cache");
		personCacheConfig.setIndexedTypes(PersonKey.class, Person.class);
		personCacheConfig.setCacheMode(CacheMode.PARTITIONED);
		personCacheConfig.setSqlSchema("ip");

		ignite.addCacheConfiguration(personCacheConfig);
		ignite.getOrCreateCache(personCacheConfig.getName());

		CacheConfiguration<Integer, Person> personStatusCacheConfig = new CacheConfiguration<>("person-status-cache");
		personStatusCacheConfig.setIndexedTypes(Integer.class, PersonStatus.class);
		personStatusCacheConfig.setCacheMode(CacheMode.PARTITIONED);
		personStatusCacheConfig.setSqlSchema("ip");

		ignite.addCacheConfiguration(personStatusCacheConfig);
		ignite.getOrCreateCache(personStatusCacheConfig.getName());
	}

	private void init() {
		log.debug("init service");

		createCaches();
		start();
	}

	public void main() {
		log.debug("main service");
		init();
	}
}
