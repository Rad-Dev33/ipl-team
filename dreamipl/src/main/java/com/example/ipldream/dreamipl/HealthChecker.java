package com.example.ipldream.dreamipl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthChecker {
	
	@GetMapping
	public String hello() {
		return "hello world";
	}

}
