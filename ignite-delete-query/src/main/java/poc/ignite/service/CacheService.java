package poc.ignite.service;

import java.util.List;

import javax.cache.Cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.domain.Person;
import poc.ignite.domain.PersonKey;
import poc.ignite.domain.PersonStatus;

@Slf4j
@Service
public class CacheService {

	@Autowired
	private Ignite ignite;

	private void loadCaches() {
		log.debug("loadCaches service");

		IgniteCache<PersonKey, Person> personCache = ignite.cache("person-cache");
		IgniteCache<Integer, PersonStatus> personStatusCache = ignite.cache("person-status-cache");

		for (int i = 1; i <= 50; i++) {
			personStatusCache.put(i, new PersonStatus(i, 1));

			for (int j = 1; j <= 50; j++)
				personCache.put(new PersonKey(i, "p" + j), new Person(i, "p" + j, i, i));
		}
	}

	private void cacheKeys() {
		log.debug("cacheKeys service");

		IgniteCache<PersonKey, Person> personCache = ignite.cache("person-cache");

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

		loadCaches();
		// cacheKeys();
	}
}
