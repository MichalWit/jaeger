package org.example.actuator;

import org.springframework.beans.factory.annotation.Autowired;
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

	@RequestMapping("/call-actuator-service")
	public String callActuatorService() {
		final String restTemplateResp = restTemplate
				.getForObject("http://actuator/hello", String.class);
		final String webClientResp = webClient
				.get()
				.uri("http://actuator/hello")
				.exchange()
				.block()
				.bodyToMono(String.class)
				.block();

		System.out.println("RestTemplate response: " + restTemplateResp);
		System.out.println("WebClient response: " + webClientResp);

		return restTemplateResp + "  |  " + webClientResp;
	}
}
