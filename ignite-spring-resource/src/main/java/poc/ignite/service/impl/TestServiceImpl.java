package poc.ignite.service.impl;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.ignite.service.TestService;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

	@Override
	public void run() {
		log.debug("TestService run method");
	}

}
