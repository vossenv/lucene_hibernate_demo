package com.dm.teamquery;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableSpringDataWebSupport
@SpringBootApplication
public class TeamqueryApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(TeamqueryApplication.class)
		.properties("spring.config.name:application,database").build().run(args);
    }
}

