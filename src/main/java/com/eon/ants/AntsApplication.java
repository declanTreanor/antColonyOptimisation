package com.eon.ants;

//import jdk.incubator.concurrent.StructuredTaskScope;

import com.eon.ants.config.ACOConfig;
import com.eon.ants.domain.Ant;
import com.eon.ants.service.PheremoneManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class AntsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntsApplication.class, args);
	}
}

