package com.afourathon.project_management_rest_api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.afourathon.project_management_rest_api.data.entity.MailingList;
import com.afourathon.project_management_rest_api.data.entity.Project;
import com.afourathon.project_management_rest_api.data.payloads.repository.MailingListRepository;
import com.afourathon.project_management_rest_api.data.payloads.repository.ProjectRepository;
import com.afourathon.project_management_rest_api.data.payloads.request.ProjectRequest;
import com.afourathon.project_management_rest_api.service.JpaMailingListService;
import com.afourathon.project_management_rest_api.service.JpaProjectService;
import com.afourathon.project_management_rest_api.service.MailingListService;
import com.afourathon.project_management_rest_api.service.ProjectService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProjectServiceTests {

	@InjectMocks
	private ProjectService projectService = new JpaProjectService();

	@Mock
	private ProjectRepository projectRepository;

	@InjectMocks
	private MailingListService mailingListService = new JpaMailingListService();

	@Mock
	private MailingListRepository mailingListRepository;

	@Test
	public void findProjectByIdTest() {
		Project project = getProjectDetails();
		Long projectId = project.getId();
		
		Optional<Project> expectedOptProjectObj = Optional.of(project);

		when(projectRepository.findById(projectId)).thenReturn(expectedOptProjectObj);
		
		Optional<Project> actualOptProjectObj = projectService.findProjectById(projectId);
		
		Project expectedProject = expectedOptProjectObj.get();
		Project actualProject = actualOptProjectObj.get();

		assertTrue(actualOptProjectObj.isPresent());
		assertEquals(expectedProject, actualProject);
		
		assertAll(
				() -> assertEquals(expectedProject.getId(), actualProject.getId()),
				() -> assertEquals(expectedProject.getName(), actualProject.getName()),
				() -> assertEquals(expectedProject.getManagerName(), actualProject.getManagerName()),
				() -> assertEquals(expectedProject.getManagerEmail(), actualProject.getManagerEmail())
		);
		
		verify(projectRepository, times(1)).findById(projectId);
	}

	@Test
	public void findAllProjectsTest() {
		Project project = getProjectDetails();

		when(projectRepository.findAll()).thenReturn(
				Stream.of(project)
				.collect(Collectors.toList()));
		
		List<Project> actualListOfProjects = projectService.findAllProjects();

		assertEquals(1, actualListOfProjects.size());
		
		verify(projectRepository, times(1)).findAll();
	}

	@Test
	public void addProjectTest() {
		ProjectRequest projectRequest = new ProjectRequest("Test Spring Boot Project", new String("1970-01-01"), 
				new String("1970-12-31"), "John Smith", "john.smith@myorg.com");

		Project expectedProject = getProjectDetailsBasedOnProjectRequest(projectRequest);

		when(projectRepository.save(any(Project.class))).thenReturn(expectedProject);
		
		Project actualProject = projectService.addProject(projectRequest);

		assertEquals(expectedProject, actualProject);
		
		assertAll(
				() -> assertEquals(expectedProject.getId(), actualProject.getId()),
				() -> assertEquals(expectedProject.getName(), actualProject.getName()),
				() -> assertEquals(expectedProject.getManagerName(), actualProject.getManagerName()),
				() -> assertEquals(expectedProject.getManagerEmail(), actualProject.getManagerEmail())
		);
		
		verify(projectRepository, times(1)).save(any(Project.class));
	}

	@Test
	public void updateProjectTest() {
		String projectNameToBeUpdated = "Spring Boot Project - #1";
		String managerEmailToBeUpdated = "johnny.smith@myinternationalorg.com";

		ProjectRequest projectRequest = new ProjectRequest(projectNameToBeUpdated, new String("2023-01-01"), 
				new String("2023-12-31"), "John Smith", managerEmailToBeUpdated);

		Project expectedProject = getProjectDetailsBasedOnProjectRequest(projectRequest);
		Long projectId = expectedProject.getId();
		
		Optional<Project> expectedOptProjectObj =  Optional.of(expectedProject);
		
		when(projectRepository.findById(projectId)).thenReturn(expectedOptProjectObj);
		when(projectRepository.save(any(Project.class))).thenReturn(expectedOptProjectObj.get());
		
		Project actualProject = projectService.updateProject(projectId, projectRequest);

		assertEquals(expectedProject, actualProject);
		assertTrue(actualProject.getName().equalsIgnoreCase(expectedProject.getName()));
		assertTrue(actualProject.getManagerEmail().equalsIgnoreCase(expectedProject.getManagerEmail()));
		
		verify(projectRepository, times(1)).findById(projectId);
		verify(projectRepository, times(1)).save(any(Project.class));
	}

	@Test
	public void assignMailToProjectTest() {
		Project expectedProject = getProjectDetails();
		Long projectId = expectedProject.getId();
		
		Optional<Project> expectedOptProjectObj = Optional.of(expectedProject);
		when(projectRepository.findById(anyLong())).thenReturn(expectedOptProjectObj);
		
		Long mailId = 103L;
		MailingList emailToBeAdded = new MailingList(mailId, "Joe Doe", "joe.doe@myorg.com");
		Optional<MailingList> optEmailObj = Optional.of(emailToBeAdded);
		when(mailingListRepository.findById(anyLong())).thenReturn(optEmailObj);
		
		when(projectRepository.save(any(Project.class))).thenReturn(expectedProject);
		
		Project actualProject = projectService.assignMailToProject(projectId, mailId);
		
		assertEquals(expectedProject, actualProject);
		assertEquals(3, actualProject.getMailingList().size());

		assertTrue(actualProject.getMailingList().contains(emailToBeAdded));
		
		verify(projectRepository, atLeast(1)).findById(anyLong());
		verify(projectRepository, atLeast(1)).save(any(Project.class));
		verify(mailingListRepository, atLeast(1)).findById(anyLong());
	}

	@Test
	public void removeAssignedMailFromProject() {
		Project expectedProject = getProjectDetails();
		Long projectId = expectedProject.getId();
		
		Optional<Project> expectedOptProjectObj = Optional.of(expectedProject);
		when(projectRepository.findById(anyLong())).thenReturn(expectedOptProjectObj);
		
		Long mailId = 102L;
		Optional<MailingList> optEmailObj = expectedProject.getMailingList().stream()
			.filter(email -> email.getId() == mailId)
			.findFirst();
		MailingList emailToBeRemoved = optEmailObj.get();
		when(mailingListRepository.findById(anyLong())).thenReturn(optEmailObj);
		
		when(projectRepository.save(any(Project.class))).thenReturn(expectedProject);
		
		Project actualProject = projectService.removeAssignedMailFromProject(projectId, mailId);
		
		assertEquals(expectedProject, actualProject);
		assertEquals(1, actualProject.getMailingList().size());

		assertFalse(actualProject.getMailingList().contains(emailToBeRemoved));
		
		verify(projectRepository, atLeast(1)).findById(anyLong());
		verify(projectRepository, atLeast(1)).save(any(Project.class));
		verify(mailingListRepository, atLeast(1)).findById(anyLong());
	}

	@Test
	public void deleteProjectTest() {
		Project project = getProjectDetails();

		projectService.deleteProject(project);

		verify(projectRepository, times(1)).delete(project);
	}

	@Test
	public void deleteAllProjectsTest() {
		projectService.deleteAllProjects();

		verify(projectRepository, times(1)).deleteAll();;
	}
	
	private Project getProjectDetails() {
		Long projectId = 1001L;
		
		Project project = new Project();
		project.setId(projectId);
		project.setName("Test Spring Boot Project");
		project.setStartDate(LocalDate.parse(new String("2022-01-01")));
		project.setEndDate(LocalDate.parse(new String("2022-12-31")));
		project.setManagerName("John Smith");
		project.setManagerEmail("john.smith@myorg.com");
		
		project.setMailingList(getMailingListDetails());
		
		return project;
	}
	
	private Project getProjectDetailsBasedOnProjectRequest(ProjectRequest request) {
		Long projectId = 1001L;
		
		Project project = new Project();
		project.setId(projectId);
		project.setName(request.getName());
		project.setStartDate(LocalDate.parse(request.getStartDate()));
		project.setEndDate(LocalDate.parse(request.getEndDate()));
		project.setManagerName(request.getManagerName());
		project.setManagerEmail(request.getManagerEmail());
		
		project.setMailingList(getMailingListDetails());
		
		return project;
	}
	
	private Set<MailingList> getMailingListDetails() {
		Long firstMailId = 101L;
		Long secondMailId = 102L;
		
		Set<MailingList> mailingList = new HashSet<>();

		mailingList.add(new MailingList(firstMailId, "John Smith", "john.smith@myorg.com"));
		mailingList.add(new MailingList(secondMailId, "Foo Bar", "foo.bar@myorg.com"));
		
		return mailingList;
	}

}
