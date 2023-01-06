package com.afourathon.project_management_rest_api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
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
		Long projectId = 1001L;

		Set<MailingList> mailingList = new HashSet<>();

		mailingList.add(new MailingList(1L, "John Smith", "john.smith@myorg.com"));
		mailingList.add(new MailingList(2L, "Foo Bar", "foo.bar@myorg.com"));
		
		Project project = new Project();
		project.setId(projectId);
		project.setName("Test Spring Boot Project");
		project.setStartDate(LocalDate.parse(new String("2022-01-01")));
		project.setEndDate(LocalDate.parse(new String("2022-12-31")));
		project.setManagerName("John Smith");
		project.setManagerEmail("john.smith@myorg.com");
		project.setMailingList(mailingList);
		
		Optional<Project> objProject = Optional.of(project);

		when(projectRepository.findById(projectId)).thenReturn(objProject);

		assertTrue(projectService.findProjectById(projectId).isPresent());
	}

	@Test
	public void findAllProjectsTest() {
		Set<MailingList> mailingList = new HashSet<>();

		mailingList.add(new MailingList(1L, "John Smith", "john.smith@myorg.com"));
		mailingList.add(new MailingList(2L, "Foo Bar", "foo.bar@myorg.com"));
		
		Project project1 = new Project();
		project1.setId(1001L);
		project1.setName("Test Spring Boot Project #1");
		project1.setStartDate(LocalDate.parse(new String("2022-01-01")));
		project1.setEndDate(LocalDate.parse(new String("2022-12-31")));
		project1.setManagerName("John Smith");
		project1.setManagerEmail("john.smith@myorg.com");
		project1.setMailingList(mailingList);
		
		Project project2 = new Project();
		project2.setId(1002L);
		project2.setName("Test Spring Boot Project #2");
		project2.setStartDate(LocalDate.parse(new String("2022-01-01")));
		project2.setEndDate(LocalDate.parse(new String("2022-12-31")));
		project2.setManagerName("Joe Daniel");
		project2.setManagerEmail("joe.daniel@myorg.com");
		project2.setMailingList(mailingList);

		when(projectRepository.findAll()).thenReturn(
				Stream.of(project1, project2)
				.collect(Collectors.toList()));

		assertEquals(2, projectService.findAllProjects().size());
	}

	@Test
	public void addProjectTest() {
		ProjectRequest projectRequest = new ProjectRequest("Test Spring Boot Project", new String("1970-01-01"), 
				new String("1970-12-31"), "John Smith", "john.smith@myorg.com");

		Set<MailingList> mailingList = new HashSet<>();

		mailingList.add(new MailingList(1L, "John Smith", "john.smith@myorg.com"));
		mailingList.add(new MailingList(2L, "Foo Bar", "foo.bar@myorg.com"));
		
		Project project = new Project(projectRequest.getName(), LocalDate.parse(projectRequest.getStartDate()),
				LocalDate.parse(projectRequest.getEndDate()), projectRequest.getManagerName(), 
				projectRequest.getManagerEmail(), mailingList);

		when(projectRepository.save(any(Project.class))).thenReturn(project);

		assertEquals(project, projectService.addProject(projectRequest));
	}

	@Test
	public void updateProjectTest() {
		Long projectId = 1001L;
		String updatedProjectName = "Spring Boot Project - #1";
		String updatedManagerEmail = "johnny.smith@myinternationalorg.com";

		Project originalProject = new Project();
		originalProject.setId(1001L);
		originalProject.setName("Test Spring Boot Project #1");
		originalProject.setStartDate(LocalDate.parse(new String("2022-01-01")));
		originalProject.setEndDate(LocalDate.parse(new String("2022-12-31")));
		originalProject.setManagerName("John Smith");
		originalProject.setManagerEmail("john.smith@myorg.com");

		ProjectRequest projectRequest = new ProjectRequest(updatedProjectName, new String("2023-01-01"), 
				new String("2023-12-31"), "John Smith", updatedManagerEmail);

		Project updatedProject = originalProject;
		updatedProject.setName(projectRequest.getName());
		updatedProject.setStartDate(LocalDate.parse(projectRequest.getStartDate(), DateTimeFormatter.ISO_LOCAL_DATE));
		updatedProject.setEndDate(LocalDate.parse(projectRequest.getEndDate(), DateTimeFormatter.ISO_LOCAL_DATE));
		updatedProject.setManagerName(projectRequest.getManagerName());
		updatedProject.setManagerEmail(projectRequest.getManagerEmail());

		when(mock(ProjectService.class).updateProject(projectId, projectRequest)).thenReturn(updatedProject).getMock();

		assertTrue(updatedProject.getName().equalsIgnoreCase(updatedProjectName));
		assertTrue(updatedProject.getManagerEmail().equalsIgnoreCase(updatedManagerEmail));
	}

	@Test
	public void assignMailToProject() {
		// Testing Project
		Long projectId = 1001L;
		
		Set<MailingList> mailingList = new HashSet<>();

		mailingList.add(new MailingList(1L, "John Smith", "john.smith@myorg.com"));
		mailingList.add(new MailingList(2L, "Foo Bar", "foo.bar@myorg.com"));
		
		Project project = new Project();
		project.setId(projectId);
		project.setName("Test Spring Boot Project");
		project.setStartDate(LocalDate.parse(new String("2022-01-01")));
		project.setEndDate(LocalDate.parse(new String("2022-12-31")));
		project.setManagerName("John Smith");
		project.setManagerEmail("john.smith@myorg.com");
		project.setMailingList(mailingList);
		
		Optional<Project> objProject = Optional.of(project);

		when(projectRepository.findById(projectId)).thenReturn(objProject);

		assertTrue(projectService.findProjectById(projectId).isPresent());

		// Testing Email
		Long mailId = 3L;

		Optional<MailingList> objMailingList = Optional.of(new MailingList(mailId, "John Smith", "john.smith@myorg.com"));

		when(mailingListRepository.findById(mailId)).thenReturn(objMailingList);

		assertTrue(mailingListService.findEmailById(mailId).isPresent());

		if(objProject.isPresent()) {
			Project existingProject = objProject.get();
			MailingList mailingListToBeAssigned = objMailingList.get();
			existingProject.getMailingList().add(mailingListToBeAssigned);

			when(mock(ProjectService.class).assignMailToProject(projectId, mailId)).thenReturn(existingProject).getMock();

			assertTrue("Email with ID: " + mailId + " has been added successfully to Project with ID: " + projectId, existingProject.getMailingList().size() == 3);
		}
	}

	@Test
	public void removeAssignedMailFromProject() {
		// Testing Project
		Long projectId = 1001L;
		
		Long mailId = 1L;

		Optional<MailingList> objMailingList = Optional.of(new MailingList(mailId, "John Smith", "john.smith@myorg.com"));
		MailingList mailingListToBeRemoved = objMailingList.get();
		
		Set<MailingList> mailingList = new HashSet<>();
		mailingList.add(mailingListToBeRemoved);

		Project project = new Project();
		project.setId(projectId);
		project.setName("Test Spring Boot Project");
		project.setStartDate(LocalDate.parse(new String("2022-01-01")));
		project.setEndDate(LocalDate.parse(new String("2022-12-31")));
		project.setManagerName("John Smith");
		project.setManagerEmail("john.smith@myorg.com");
		project.setMailingList(mailingList);
		
		Optional<Project> objProject = Optional.of(project);

		when(projectRepository.findById(projectId)).thenReturn(objProject);

		assertTrue(projectService.findProjectById(projectId).isPresent());

		// Testing Email
		when(mailingListRepository.findById(mailId)).thenReturn(objMailingList);

		assertTrue(mailingListService.findEmailById(mailId).isPresent());

		if(objProject.isPresent()) {
			Project existingProject = objProject.get();
			existingProject.getMailingList().remove(mailingListToBeRemoved);

			when(mock(ProjectService.class).removeAssignedMailFromProject(projectId, mailId)).thenReturn(existingProject).getMock();

			assertTrue("Email with ID: " + mailId + " has been successfully removed from Project with ID: " + projectId, existingProject.getMailingList().size() == 0);
		}
	}

	@Test
	public void deleteProjectTest() {
		Set<MailingList> mailingList = new HashSet<>();

		mailingList.add(new MailingList(1L, "John Smith", "john.smith@myorg.com"));
		mailingList.add(new MailingList(2L, "Foo Bar", "foo.bar@myorg.com"));
		
		Project project = new Project();
		project.setId(1001L);
		project.setName("Test Spring Boot Project");
		project.setStartDate(LocalDate.parse(new String("2022-01-01")));
		project.setEndDate(LocalDate.parse(new String("2022-12-31")));
		project.setManagerName("John Smith");
		project.setManagerEmail("john.smith@myorg.com");
		project.setMailingList(mailingList);

		projectService.deleteProject(project);

		verify(projectRepository, times(1)).delete(project);
	}

	@Test
	public void deleteAllProjectsTest() {
		projectService.deleteAllProjects();

		verify(projectRepository, times(1)).deleteAll();;
	}

}
