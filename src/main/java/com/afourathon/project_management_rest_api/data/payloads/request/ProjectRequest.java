package com.afourathon.project_management_rest_api.data.payloads.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectRequest {
	
	@NotBlank(message = "Project name cannot be null.")
	private String name;
	
	@NotBlank(message = "Project's start date cannot be null.")
	private String startDate;
	
	private String endDate;
	
	@NotBlank(message = "Manager name cannot be null.")
	private String managerName;
	
	@NotBlank(message = "Manager email cannot be null.")
	private String managerEmail;
	
}
