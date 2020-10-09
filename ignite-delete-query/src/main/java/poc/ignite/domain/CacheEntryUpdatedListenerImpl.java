package poc.ignite.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.resources.IgniteInstanceResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheEntryUpdatedListenerImpl implements CacheEntryUpdatedListener<Integer, PersonStatus> {

	@IgniteInstanceResource
	private Ignite ignite;

	@Override
	public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends PersonStatus>> events)
			throws CacheEntryListenerException {

		IgniteCache<PersonKey, Person> personCache = ignite.cache("person-cache");

		for (CacheEntryEvent<? extends Integer, ? extends PersonStatus> cacheEntryEvent : events) {
			log.debug("type: " + cacheEntryEvent.getEventType() + ", value: " + cacheEntryEvent.getValue());

			Thread t = new Thread(() -> {
				int id = cacheEntryEvent.getKey();

				while (true) {

					SqlFieldsQuery keys = new SqlFieldsQuery("SELECT _key from Person WHERE id=? limit 10").setArgs(id);
					Set<PersonKey> keySet = new HashSet<>();

					try (QueryCursor<List<?>> cursor = personCache.query(keys)) {
						cursor.forEach(r -> {
							keySet.add((PersonKey) r.get(0));
						});
					}

					if (keySet.size() == 0) {
						IgniteCache<Integer, PersonStatus> personStatusCache = ignite.cache("person-status-cache");
						SqlFieldsQuery updateStatus = new SqlFieldsQuery("update PersonStatus set status=3 where id=?")
								.setArgs(id);
						personStatusCache.query(updateStatus);

						break;
					}

					log.debug("keySet: " + keySet);

					personCache.clearAll(keySet);

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						log.error(e.getMessage(), e);
					}
				}
			});

			// t.setName("delete-query-thread");
			t.start();
		}
	}
}
