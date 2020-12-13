package gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	@Autowired
	private Config config;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
			.route(p -> p
				.path("/routed")
				.filters(f -> f
						.addRequestHeader("X-Gateway", "custom-header")
						.rewritePath("routed", "actuator")
				)
				.uri("http://" + config.getActuatorHost())
			)
			.route(p -> p
				.path("/mediator")
				.filters(f -> f
						.addRequestHeader("X-Gateway", "_custom_header_")
						.rewritePath("mediator", "call-actuator-service")
				)
				.uri("http://" + config.getMediatorHost())
			)
			.build();
	}

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}

	@Configuration
	@ConfigurationProperties(prefix = "services")
	static class Config {

		private String actuatorHost;
		private String mediatorHost;

		public String getActuatorHost() {
			return actuatorHost;
		}

		public void setActuatorHost(String actuatorHost) {
			this.actuatorHost = actuatorHost;
		}

		public String getMediatorHost() {
			return mediatorHost;
		}

		public void setMediatorHost(String mediatorHost) {
			this.mediatorHost = mediatorHost;
		}
	}
}
