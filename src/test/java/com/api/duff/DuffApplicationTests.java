package com.api.duff;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(loader = SpringBootContextLoader.class, classes = DuffApplication.class)
public class DuffApplicationTests {

	@Test
	void contextLoads() {
	}

}

