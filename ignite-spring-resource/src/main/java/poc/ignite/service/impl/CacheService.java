package poc.ignite.service.impl;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.domain.Person;

@Service
@Slf4j
public class CacheService {

//	@Autowired
//	private Ignite ignite;
	@Autowired
	private IgniteSpringBean igniteSpringBean;

	private void loadCaches() {
		log.debug("loadCaches service");

		IgniteCache<Integer, Person> personCache = igniteSpringBean.cache("person-cache");

		for (int i = 1; i <= 50; i++) {
			personCache.put(i, new Person(i, "p" + i, i, i));
		}
	}

	private void createCaches() {
		log.debug("createCaches service");

		CacheConfiguration<Integer, Person> personCacheConfig = new CacheConfiguration<>("person-cache");
		personCacheConfig.setIndexedTypes(Integer.class, Person.class);
		personCacheConfig.setCacheMode(CacheMode.PARTITIONED);
		personCacheConfig.setSqlSchema("ip");

		igniteSpringBean.addCacheConfiguration(personCacheConfig);
		igniteSpringBean.getOrCreateCache(personCacheConfig.getName());
	}

	private void init() {
		log.debug("init service");

		createCaches();
		loadCaches();
	}

	public void main() {
		log.debug("main service");
		init();
	}
}
