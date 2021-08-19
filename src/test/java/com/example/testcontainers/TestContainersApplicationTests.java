package com.example.testcontainers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class TestContainersApplicationTests {

	@Autowired
	private CustomerDao customerDao;

	//@Container
	private static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:8.0.26")
			.withReuse(true);

	@BeforeAll
	public static void setup() {
		mySQLContainer.start();
	}

	//private static GenericContainer gc = new GenericContainer("image:version");

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
	}

	@Test
	void when_using_a_clean_db_this_should_be_empty() {
		//mySQLContainer.withClasspathResourceMapping("application.properties", "/tmp/application.properties", BindMode.READ_ONLY);
		//mySQLContainer.withFileSystemBind("application.properties", "/tmp/application.properties", BindMode.READ_ONLY);
		//mySQLContainer.execInContainer("ls");
		//mySQLContainer.getLogs(OutputFrame.OutputType.STDOUT);
		//mySQLContainer.withLogConsumer(new Slf4jLogConsumer());
		//var portOnYourMachine = mySQLContainer.getMappedPort(3306);

		var customers = customerDao.findAll();
		assertThat(customers).hasSize(2);
	}
}
