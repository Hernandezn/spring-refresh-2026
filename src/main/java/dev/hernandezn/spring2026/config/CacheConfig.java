package dev.hernandezn.spring2026.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {
	
	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager manager = new CaffeineCacheManager();
		
		manager.registerCustomCache(
			"runtimeId",
			Caffeine.newBuilder()
				.maximumSize(1L)
				// not specifying this will keep item in cache indefinitely, until a @CacheEvict("runtimeId")
//				.expireAfterWrite(10, TimeUnit.MINUTES)
				.build()
		);
		
		return manager;
	}
}
