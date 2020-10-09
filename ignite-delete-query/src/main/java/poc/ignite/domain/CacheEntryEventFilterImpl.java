package poc.ignite.domain;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;

public class CacheEntryEventFilterImpl implements CacheEntryEventFilter<Integer, PersonStatus> {

	@Override
	public boolean evaluate(CacheEntryEvent<? extends Integer, ? extends PersonStatus> event)
			throws CacheEntryListenerException {
		return event.getValue().getStatus() == 2;
	}
}
