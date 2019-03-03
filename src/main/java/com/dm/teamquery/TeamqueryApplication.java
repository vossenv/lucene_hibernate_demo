package com.dm.teamquery;

import com.dm.teamquery.data.repository.Base.CustomRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
public class TeamqueryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamqueryApplication.class, args);
	}

}
