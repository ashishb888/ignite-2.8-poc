package poc.ignite.service;

public interface TestService {
	void run();

	long call(int affKey, String cacheName);
}
