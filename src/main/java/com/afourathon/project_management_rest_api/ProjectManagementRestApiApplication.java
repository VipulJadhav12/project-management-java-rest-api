package com.afourathon.project_management_rest_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ProjectManagementRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementRestApiApplication.class, args);
	}

}
