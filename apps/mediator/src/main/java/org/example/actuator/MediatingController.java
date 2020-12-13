package org.example.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class MediatingController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private WebClient webClient;

	@Autowired
	private Config config;

	@RequestMapping("/call-actuator-service")
	public String callActuatorService() {
		final String restTemplateResp = restTemplate
				.getForObject("http://" + config.getHost() + "/hello", String.class);
		final String webClientResp = webClient
				.get()
				.uri("http://" + config.getHost() + "/hello")
				.exchange()
				.block()
				.bodyToMono(String.class)
				.block();

		System.out.println("RestTemplate response: " + restTemplateResp);
		System.out.println("WebClient response: " + webClientResp);

		return restTemplateResp + "  |  " + webClientResp;
	}

	@Configuration
	@ConfigurationProperties(prefix = "actuator-service")
	static class Config {

		private String host;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}
	}
}
