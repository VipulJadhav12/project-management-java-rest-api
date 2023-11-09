package com.afourathon.project_management_rest_api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afourathon.project_management_rest_api.data.entity.MailingList;
import com.afourathon.project_management_rest_api.data.entity.Project;
import com.afourathon.project_management_rest_api.data.payloads.repository.MailingListRepository;
import com.afourathon.project_management_rest_api.data.payloads.repository.ProjectRepository;
import com.afourathon.project_management_rest_api.data.payloads.request.ProjectRequest;

@Service
public class JpaProjectService implements ProjectService {

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	MailingListRepository mailingListRepository;

	@Override
	public boolean checkIfProjectExistsById(Long projectId) {
		return projectRepository.existsById(projectId);
	}

	/* ========================================
	 * List of service fns to FIND Projects
	 * based on conditions.
	 * ========================================
	 */

	@Override
	public Optional<Project> findProjectById(Long projectId) {
		return projectRepository.findById(projectId);
	}

	@Override
	public Optional<Project> findProjectByName(String projectName) {
		return projectRepository.findByName(projectName);
	}

	@Override
	public List<Project> findAllProjects() {
		return projectRepository.findAll();
	}

	@Override
	public Page<Project> findAllProjectsSortedByFieldName(String field, int offset, int pageSize) {
		return projectRepository.findAll(PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.ASC, field)));
	}

	@Override
	public Page<Project> findAllProjectsSortedByFieldNameInDesc(String field, int offset, int pageSize) {
		return projectRepository.findAll(PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, field)));
	}

	/* ========================================
	 * List of service fns to CREATE and 
	 * UPDATE Projects.
	 * ========================================
	 */

	@Override
	public Project addProject(ProjectRequest projectRequest) {
		Project project = new Project();
		project.setName(projectRequest.getName());
		project.setStartDate(LocalDate.parse(projectRequest.getStartDate(), DateTimeFormatter.ISO_LOCAL_DATE));
		project.setEndDate(LocalDate.parse(projectRequest.getEndDate(), DateTimeFormatter.ISO_LOCAL_DATE));
		project.setManagerName(projectRequest.getManagerName());
		project.setManagerEmail(projectRequest.getManagerEmail());

		try {
			return projectRepository.save(project);
		}
		catch(IllegalArgumentException ex) {
			return null;
		}
		catch(OptimisticLockingFailureException ex) {
			return null;
		}
	}

	@Override
	public Project updateProject(Long projectId, ProjectRequest projectRequest) {
		Optional<Project> objProject = projectRepository.findById(projectId);

		if(objProject.isPresent()) {
			Project existingProject = objProject.get();
			existingProject.setName(projectRequest.getName());
			existingProject.setStartDate(LocalDate.parse(projectRequest.getStartDate(), DateTimeFormatter.ISO_LOCAL_DATE));
			existingProject.setEndDate(LocalDate.parse(projectRequest.getEndDate(), DateTimeFormatter.ISO_LOCAL_DATE));
			existingProject.setManagerName(projectRequest.getManagerName());
			existingProject.setManagerEmail(projectRequest.getManagerEmail());

			try {
				return projectRepository.save(existingProject);
			}
			catch(IllegalArgumentException ex) {
				return null;
			}
			catch(OptimisticLockingFailureException ex) {
				return null;
			}
		}

		return null;
	}

	@Override
	public Project assignMailToProject(Long projectId, Long mailId) {
		Optional<Project> objProject = projectRepository.findById(projectId);
		Optional<MailingList> objMailingList = mailingListRepository.findById(mailId);

		if(objProject.isPresent()) {

			if(objMailingList.isPresent()) {
				Project existingProject = objProject.get();
				MailingList existingEmail = objMailingList.get();

				try {
					Set<MailingList> setOfEmails = existingProject.getMailingList();
					setOfEmails.add(existingEmail);

					existingProject.setMailingList(setOfEmails);
					return projectRepository.save(existingProject);
				}
				catch(IllegalArgumentException ex) {
					return null;
				}
				catch(OptimisticLockingFailureException ex) {
					return null;
				}
				catch(Exception ex) {
					return null;
				}
			}

		}

		return null;
	}

	@Override
	public Project removeAssignedMailFromProject(Long projectId, Long mailId) {
		Optional<Project> objProject = projectRepository.findById(projectId);
		Optional<MailingList> objMailingList = mailingListRepository.findById(mailId);

		if(objProject.isPresent()) {

			if(objMailingList.isPresent()) {
				Project existingProject = objProject.get();
				MailingList existingEmail = objMailingList.get();

				try {
					Set<MailingList> setOfEmails = existingProject.getMailingList();
					setOfEmails.remove(existingEmail);

					existingProject.setMailingList(setOfEmails);
					return projectRepository.save(existingProject);
				}
				catch(IllegalArgumentException ex) {
					return null;
				}
				catch(OptimisticLockingFailureException ex) {
					return null;
				}
				catch(Exception ex) {
					return null;
				}
			}

		}

		return null;
	}

	/* ========================================
	 * List of service fns to DELETE Project(s)
	 * ========================================
	 */

	@Transactional
	@Override
	public boolean deleteProject(Project project) {
		Long projectId = project.getId();

		try {
			projectRepository.delete(project);

			Optional<Project> objProject = projectRepository.findById(projectId);

			if(!objProject.isPresent())
				return true;
		}
		catch(IllegalArgumentException ex) {
			return false;
		}
		catch(OptimisticLockingFailureException ex) {
			return false;
		}

		return false;

	}

	@Transactional
	@Override
	public boolean deleteAllProjects() {
		projectRepository.deleteAll();

		if(0 == projectRepository.count())
			return true;

		return false;
	}

}
