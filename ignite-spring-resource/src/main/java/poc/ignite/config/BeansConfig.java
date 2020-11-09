package poc.ignite.config;

import java.util.Arrays;

import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BeansConfig {

	@Bean
	public IgniteSpringBean igniteSpringBean() {
		log.debug("igniteSpringBean service");

		IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
		igniteSpringBean.setConfiguration(igniteConfiguration());

		return igniteSpringBean;
	}

	private IgniteConfiguration igniteConfiguration() {
		log.debug("igniteConfiguration service");

		TcpDiscoverySpi spi = new TcpDiscoverySpi();

		spi.setLocalPort(42500);
		spi.setLocalPortRange(100);

		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

		// ipFinder.setAddresses(Arrays.asList("172.17.104.233:42500..42700"));
		ipFinder.setAddresses(Arrays.asList("localhost:42500..42700"));

		spi.setIpFinder(ipFinder);
		IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

		// Changing total RAM size to be used by Ignite Node.
		DataStorageConfiguration storageCfg = new DataStorageConfiguration();

		storageCfg.getDefaultDataRegionConfiguration().setInitialSize(256 * 1024 * 1024);
		// Setting the size of the default memory region to *GB to achieve this.
		storageCfg.getDefaultDataRegionConfiguration().setMaxSize(256 * 1024 * 1024);

		igniteConfiguration.setDataStorageConfiguration(storageCfg);

		igniteConfiguration.setFailureDetectionTimeout(90000);

		TcpCommunicationSpi commSpi = new TcpCommunicationSpi();
		commSpi.setLocalPort(42100);

		commSpi.setMessageQueueLimit(1024);
		commSpi.setSocketWriteTimeout(10000L);

		igniteConfiguration.setCommunicationSpi(commSpi);

		// All properties should be in YAML
		igniteConfiguration.setDiscoverySpi(spi);
		igniteConfiguration.setIncludeEventTypes();
		igniteConfiguration.setPublicThreadPoolSize(16);
		igniteConfiguration.setSystemThreadPoolSize(16);
		igniteConfiguration.setPeerClassLoadingEnabled(true);
		igniteConfiguration.setGridLogger(new Slf4jLogger());
		igniteConfiguration.setWorkDirectory("/var/tmp/ignite/work");

		return igniteConfiguration;
	}

//	@Bean
//	public Ignite ignite() {
//		log.info("ignite bean service");
//
//		Ignite ignite = null;
//
//		try {
//
//			TcpDiscoverySpi spi = new TcpDiscoverySpi();
//
//			spi.setLocalPort(42500);
//			spi.setLocalPortRange(100);
//
//			TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
//
//			// ipFinder.setAddresses(Arrays.asList("172.17.104.233:42500..42700"));
//			ipFinder.setAddresses(Arrays.asList("localhost:42500..42700"));
//
//			spi.setIpFinder(ipFinder);
//			IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
//
//			// Changing total RAM size to be used by Ignite Node.
//			DataStorageConfiguration storageCfg = new DataStorageConfiguration();
//
//			storageCfg.getDefaultDataRegionConfiguration().setInitialSize(256 * 1024 * 1024);
//			// Setting the size of the default memory region to *GB to achieve this.
//			storageCfg.getDefaultDataRegionConfiguration().setMaxSize(256 * 1024 * 1024);
//
//			igniteConfiguration.setDataStorageConfiguration(storageCfg);
//
//			igniteConfiguration.setFailureDetectionTimeout(90000);
//
//			TcpCommunicationSpi commSpi = new TcpCommunicationSpi();
//			commSpi.setLocalPort(42100);
//
//			commSpi.setMessageQueueLimit(1024);
//			commSpi.setSocketWriteTimeout(10000L);
//
//			igniteConfiguration.setCommunicationSpi(commSpi);
//
//			// All properties should be in YAML
//			igniteConfiguration.setDiscoverySpi(spi);
//			igniteConfiguration.setIncludeEventTypes();
//			igniteConfiguration.setPublicThreadPoolSize(16);
//			igniteConfiguration.setSystemThreadPoolSize(16);
//			igniteConfiguration.setPeerClassLoadingEnabled(true);
//			igniteConfiguration.setGridLogger(new Slf4jLogger());
//			igniteConfiguration.setWorkDirectory("/var/tmp/ignite/work");
//
//			ignite = Ignition.getOrStart(igniteConfiguration);
//
//		} catch (IgniteException e) {
//			log.error(e.getMessage(), e);
//		}
//
//		return ignite;
//	}
}
