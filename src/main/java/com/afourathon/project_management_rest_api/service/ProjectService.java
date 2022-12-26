package com.afourathon.project_management_rest_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.afourathon.project_management_rest_api.data.entity.Project;
import com.afourathon.project_management_rest_api.data.payloads.request.ProjectRequest;

public interface ProjectService {
	
	public boolean checkIfProjectExistsById(Long projectId);
	
	/* ========================================
	 * List of service fns to FIND Projects
	 * based on conditions.
	 * ========================================
	 */
	public Optional<Project> findProjectById(Long projectId);
	
	public Optional<Project> findProjectByName(String projectName);
	
	public List<Project> findAllProjects();
	
	public Page<Project> findAllProjectsSortedByFieldName(String field, int offset, int pageSize);
	
	public Page<Project> findAllProjectsSortedByFieldNameInDesc(String field, int offset, int pageSize);
	
	/* ========================================
	 * List of service fns to CREATE and 
	 * UPDATE Projects.
	 * ========================================
	 */
	public Project addProject(ProjectRequest projectRequest);
	
	public Project updateProject(Long projectId, ProjectRequest projectRequest);
	
	public Project assignMailToProject(Long projectId, Long mailId);
	
	public Project removeAssignedMailFromProject(Long projectId, Long mailId);

	/* ========================================
	 * List of service fns to DELETE Project(s)
	 * ========================================
	 */
	public boolean deleteProject(Project project);
	
	public boolean deleteAllProjects();

}
