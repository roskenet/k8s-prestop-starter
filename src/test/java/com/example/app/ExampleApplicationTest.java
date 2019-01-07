package com.example.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ExampleApplicationTest {

	@LocalManagementPort
	private int managementPort;

	@Autowired
	private TestRestTemplate rest;

	@Test
	public void contextLoads() {
		ResponseEntity<String> response = rest.getForEntity("http://localhost:" + managementPort + "/health",
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(OK);

		response = rest.getForEntity("http://localhost:" + managementPort + "/preStop", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("OUT_OF_SERVICE");

		response = rest.getForEntity("http://localhost:" + managementPort + "/health", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
	}
}
