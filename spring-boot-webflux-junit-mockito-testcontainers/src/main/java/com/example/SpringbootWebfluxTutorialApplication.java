package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class SpringbootWebfluxTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebfluxTutorialApplication.class, args);
	}

}