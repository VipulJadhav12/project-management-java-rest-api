package com.afourathon.project_management_rest_api.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations {
	
	public static final String INVALID_PROJECT_ID = "Invalid Project ID. Project doesn't exists with ID: %d.";
	public static final String INVALID_PROJECT_NAME = "Invalid Project Name. Project doesn't exists with Name: %s.";
	public static final String PROJECT_ADD_ON_SUCCESS = "New Project {ID: %d} has been added successfully!";
	public static final String PROJECT_ADD_ON_FAILED = "An error occured while adding new project into database.";
	public static final String PROJECT_UPDATE_ON_SUCCESS = "Project {ID: %d} has been updated successfully!";
	public static final String PROJECT_UPDATE_ON_FAILED = "An error occured while updating Project with ID: %d.";
	public static final String DELETE_PROJECT_ON_SUCCESS = "Project {ID: %d} has been deleted successfully!";
	public static final String DELETE_PROJECT_ON_FAILED = "An error occured while deleting Project with ID: %d.";
	public static final String DELETE_ALL_PROJECTS_ON_SUCCESS = "All projects has been deleted successfully!";
	public static final String DELETE_ALL_PROJECTS_ON_FAILED = "An error occured while deleting all projects.";
	
	public static final String INVALID_EMAIL_ID = "Invalid Email ID. Email doesn't exists with ID: %d";
	public static final String INVALID_EMAIL_RECIPIENT_NAME = "Invalid Recipient Name. Email doesn't exists with Recipient Name: %s";
	public static final String EMAIL_ADD_ON_SUCCESS = "New Email {ID: %d} has been added successfully!";
	public static final String EMAIL_ADD_ON_FAILED = "An error occured while adding new email into database.";
	public static final String EMAIL_UPDATE_ON_SUCCESS = "New Email {ID: %d} has been updated successfully!";
	public static final String EMAIL_UPDATE_ON_FAILED = "An error occured while updating Email with ID: %d.";
	public static final String DELETE_EMAIL_ON_SUCCESS = "Email {ID: %d} has been deleted successfully!";
	public static final String DELETE_EMAIL_ON_FAILED = "An error occured while deleting Email with ID: %d.";
	public static final String DELETE_ALL_EMAILS_ON_SUCCESS = "All emails has been deleted successfully!";
	public static final String DELETE_ALL_EMAILS_ON_FAILED = "An error occured while deleting all emails.";

}