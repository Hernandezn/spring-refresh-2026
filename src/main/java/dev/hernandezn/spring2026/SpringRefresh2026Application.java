package dev.hernandezn.spring2026;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringRefresh2026Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringRefresh2026Application.class, args);
	}

}
