package org.example.actuator;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

	@RequestMapping("/hello")
	public String index() {
		return "This is a response from 'actuator' service";
	}

}
