package poc.ignite.service;

import org.springframework.stereotype.Service;

@Service("testService")
public class TestServiceImpl implements TestService {

	@Override
	public void run() {

	}

	@Override
	public long call(int affKey, String cacheName) {
		return 0;
	}

}
