package com.dm.teamquery;


import com.dm.teamquery.data.repository.custom.TeamQueryRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = TeamQueryRepositoryImpl.class)
public class TeamqueryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamqueryApplication.class, args);
	}

}
