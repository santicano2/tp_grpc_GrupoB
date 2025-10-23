package com.example.API;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.kafka.bootstrap-servers=localhost:9092")
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
