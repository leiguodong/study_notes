package com.lei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com")
public class SpringbootRocketmqApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringbootRocketmqApplication.class, args);
	}

}
