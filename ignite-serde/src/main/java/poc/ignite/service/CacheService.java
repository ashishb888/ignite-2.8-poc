package poc.ignite.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.domain.Person;

@Slf4j
@Service
public class CacheService {

	@Autowired
	private Ignite ignite;
	private IgniteCache<Integer, Person> personCache;

	public void createCaches() {
		log.debug("createCaches service");

		CacheConfiguration<Integer, Person> personCacheConfig = new CacheConfiguration<>("person-cache");
		// personCacheConfig.setIndexedTypes(Integer.class, Person.class);
		personCacheConfig.setCacheMode(CacheMode.PARTITIONED);
		personCacheConfig.setSqlSchema("ip");

		QueryEntity queryEntity = new QueryEntity();
		queryEntity.setKeyType(Integer.class.getName());
		queryEntity.setValueType(Person.class.getName());

		LinkedHashMap<String, String> fields = new LinkedHashMap<>();
		List<Field> fieldsLs = Arrays.stream(Person.class.getDeclaredFields())
				.filter(f -> !f.getName().toLowerCase().equals("serialversionuid")).collect(Collectors.toList());
		fieldsLs.forEach(f -> fields.put(f.getName(), f.getType().getName()));

		queryEntity.setFields(fields);

		Collection<QueryIndex> indexes = new ArrayList<>(1);
		indexes.add(new QueryIndex("id"));

		queryEntity.setIndexes(indexes);
		personCacheConfig.setQueryEntities(Arrays.asList(queryEntity));

		ignite.addCacheConfiguration(personCacheConfig);

		personCache = ignite.getOrCreateCache(personCacheConfig.getName());
	}

	public void loadCaches() throws Exception {
		log.debug("loadCaches service");

		for (int i = 1; i <= 5; i++) {
			Person p = new Person(i, "person" + i, i, i);
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
