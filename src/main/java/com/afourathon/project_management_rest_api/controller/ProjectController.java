package com.afourathon.project_management_rest_api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afourathon.project_management_rest_api.configuration.Configurations;
import com.afourathon.project_management_rest_api.data.entity.Project;
import com.afourathon.project_management_rest_api.data.payloads.request.ProjectRequest;
import com.afourathon.project_management_rest_api.data.payloads.response.ApiResponse;
import com.afourathon.project_management_rest_api.exception.EmailNotFoundException;
import com.afourathon.project_management_rest_api.exception.ProjectAlreadyExistException;
import com.afourathon.project_management_rest_api.exception.ProjectNotFoundException;
import com.afourathon.project_management_rest_api.service.MailingListService;
import com.afourathon.project_management_rest_api.service.ProjectService;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	MailingListService mailingListService;
	
	@Autowired
	Configurations configurations;
	
	@GetMapping(value = {"", "/"})
	public ResponseEntity<String> defaultHealthCheck() {
		return new ResponseEntity<String>("Default HTTP Status: OK", HttpStatus.OK);
	}
	
	@GetMapping("/getBy=ID/project/{projectId}")
	public ResponseEntity<Project> getProject(@PathVariable Long projectId) throws ProjectNotFoundException {
		if(!projectService.checkIfProjectExistsById(projectId)) {
			throw new ProjectNotFoundException(String.format(Configurations.INVALID_PROJECT_ID, projectId));
		}
		
		Optional<Project> objProject = projectService.findProjectById(projectId);
		
		if(!objProject.isPresent())
			throw new ProjectNotFoundException(String.format(Configurations.INVALID_PROJECT_ID, projectId));
		
		return ResponseEntity.ok(objProject.get());
	}

	@GetMapping("/getAllBy=NONE")
	public ResponseEntity<List<Project>> getAllProjects() {
		List<Project> projects = projectService.findAllProjects();
		
		return ResponseEntity.ok(projects);
	}
	
	@GetMapping("/getBy=NAME/project/{projectName}")
	public ResponseEntity<Project> getProjectByName(@PathVariable String projectName) {
		Optional<Project> objProject = projectService.findProjectByName(projectName);
		
		if(!objProject.isPresent())
			throw new ProjectNotFoundException(String.format(Configurations.INVALID_PROJECT_NAME, projectName));
		
		return ResponseEntity.ok(objProject.get());
	}
	
	@GetMapping("/getAllBy=NONE/pagination=TRUE/offset/{offset}/pageSize/{pageSize}/sort=ASC/sortBy/{field}")
	public ResponseEntity<Page<Project>> getAllProjectsSortedByFieldName(@PathVariable int offset, @PathVariable int pageSize, @PathVariable String field) {
		Page<Project> projects = projectService.findAllProjectsSortedByFieldName(field, offset, pageSize);
		
		return ResponseEntity.ok(projects);
	}
	
	@GetMapping("/getAllBy=NONE/pagination=TRUE/offset/{offset}/pageSize/{pageSize}/sort=DESC/sortBy/{field}")
	public ResponseEntity<Page<Project>> getAllProjectsSortedByFieldNameInDesc(@PathVariable int offset, @PathVariable int pageSize, @PathVariable String field) {
		Page<Project> projects = projectService.findAllProjectsSortedByFieldNameInDesc(field, offset, pageSize);
		
		return ResponseEntity.ok(projects);
	}
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addProject(@RequestBody @Valid ProjectRequest projectRequest) {
		String projectName = projectRequest.getName();
		
		// Checking if the Project already exists with the given name
		Optional<Project> existingProject = projectService.findProjectByName(projectName);
		if(existingProject.isPresent()) {
			throw new ProjectAlreadyExistException(String.format(Configurations.PROJECT_ALREADY_EXIST, projectName));
		}
		
		Project project = projectService.addProject(projectRequest);
		
		ApiResponse apiResponse = null;
		if(null == project) {
			apiResponse = new ApiResponse(String.format(Configurations.PROJECT_ADD_ON_FAILED), HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		apiResponse = new ApiResponse(String.format(Configurations.PROJECT_ADD_ON_SUCCESS, project.getId()), HttpStatus.CREATED);
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}
	
	@PutMapping("/updateBy=ID/project/{projectId}")
	public ResponseEntity<ApiResponse> updateProject(@PathVariable Long projectId, @RequestBody @Valid ProjectRequest projectRequest) throws ProjectNotFoundException {
		if(!projectService.checkIfProjectExistsById(projectId)) {
			throw new ProjectNotFoundException(String.format(Configurations.INVALID_PROJECT_ID, projectId));
		}
		
		Project project = projectService.updateProject(projectId, projectRequest);
		
		ApiResponse apiResponse = null;
		if(null == project) {
			apiResponse = new ApiResponse(String.format(Configurations.PROJECT_UPDATE_ON_FAILED, projectId), HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		apiResponse = new ApiResponse(String.format(Configurations.PROJECT_UPDATE_ON_SUCCESS, project.getId()), HttpStatus.OK);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
	@PutMapping("/assignEmail/project/{projectId}/email/{mailId}")
	public ResponseEntity<Project> assignMailToProject(@PathVariable Long projectId, @PathVariable Long mailId) throws ProjectNotFoundException, EmailNotFoundException {
		if(!projectService.checkIfProjectExistsById(projectId)) {
			throw new ProjectNotFoundException(String.format(Configurations.INVALID_PROJECT_ID, projectId));
		}
		
		if(!mailingListService.checkIfEmailExistsById(mailId)) {
			throw new EmailNotFoundException(String.format(Configurations.INVALID_EMAIL_ID, mailId));
		}
		
		Project project = projectService.assignMailToProject(projectId, mailId);
		
		return new ResponseEntity<>(project, HttpStatus.OK);
	}
	
	@PutMapping("/removeEmail/project/{projectId}/email/{mailId}")
	public ResponseEntity<Project> removeAssignedMailFromProject(@PathVariable Long projectId, @PathVariable Long mailId) throws ProjectNotFoundException, EmailNotFoundException {
		if(!projectService.checkIfProjectExistsById(projectId)) {
			throw new ProjectNotFoundException(String.format(Configurations.INVALID_PROJECT_ID, projectId));
		}
		
		if(!mailingListService.checkIfEmailExistsById(mailId)) {
			throw new EmailNotFoundException(String.format(Configurations.INVALID_EMAIL_ID, mailId));
		}
		
		Project project = projectService.removeAssignedMailFromProject(projectId, mailId);
		
		return new ResponseEntity<>(project, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteBy=ID/project/{projectId}")
	public ResponseEntity<ApiResponse> deleteProject(@PathVariable Long projectId) throws ProjectNotFoundException {
		if(!projectService.checkIfProjectExistsById(projectId)) {
			throw new ProjectNotFoundException(String.format(Configurations.INVALID_PROJECT_ID, projectId));
		}
		
		ApiResponse apiResponse = null;
		Optional<Project> objProject = projectService.findProjectById(projectId);
		if(objProject.isPresent()) {
			Project projectToBeDeleted = objProject.get();
			boolean isDeleted = projectService.deleteProject(projectToBeDeleted);
			if(!isDeleted) {
				apiResponse = new ApiResponse(String.format(Configurations.DELETE_PROJECT_ON_FAILED, projectId), HttpStatus.INTERNAL_SERVER_ERROR);
				return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		apiResponse = new ApiResponse(String.format(Configurations.DELETE_PROJECT_ON_SUCCESS, projectId), HttpStatus.OK);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteAllBy=NONE")
	public ResponseEntity<ApiResponse> deleteAllProjects() {
		boolean isAllDeleted = projectService.deleteAllProjects();
		
		ApiResponse apiResponse = null;
		if(!isAllDeleted) {
			apiResponse = new ApiResponse(String.format(Configurations.DELETE_ALL_PROJECTS_ON_FAILED), HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		apiResponse = new ApiResponse(String.format(Configurations.DELETE_ALL_PROJECTS_ON_SUCCESS), HttpStatus.OK);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
}
