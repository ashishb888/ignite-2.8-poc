package poc.ignite.service.impl;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.domain.Person;
import poc.ignite.service.TestService;

@Slf4j
@Service("testService")
public class TestServiceImpl implements TestService {

	@Autowired
	private Ignite ignite;

	@Override
	public void run() {
		log.debug("TestService run method");
	}

	@Override
	public long call(int affKey, String cacheName) {
		log.debug("affKey: " + affKey + ", cacheName: " + cacheName);

		IgniteCache<Integer, Person> personCache = ignite.cache(cacheName);
		SqlFieldsQuery query = new SqlFieldsQuery("select _val from Person limit ?");
		query.setArgs(5);

		try (QueryCursor<List<?>> cursor = personCache.query(query)) {
			List<?> records = cursor.getAll();

			records.forEach(r -> {
				log.debug("r: " + r);
			});

			return records.size();
		}
	}
}
