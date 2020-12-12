package gateway;

import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

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
				.uri("http://actuator:80")
			)
			.route(p -> p
				.path("/mediator")
				.filters(f -> f
						.addRequestHeader("X-Gateway", "_custom_header_")
						.rewritePath("mediator", "call-actuator-service")
				)
				.uri("http://mediator:80")
			)
			.build();
	}

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}
}
