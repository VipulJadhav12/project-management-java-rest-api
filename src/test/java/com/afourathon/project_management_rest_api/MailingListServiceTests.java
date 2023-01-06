package com.afourathon.project_management_rest_api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import org.springframework.data.domain.Sort;

import com.afourathon.project_management_rest_api.data.entity.MailingList;
import com.afourathon.project_management_rest_api.data.entity.Project;
import com.afourathon.project_management_rest_api.data.payloads.repository.MailingListRepository;
import com.afourathon.project_management_rest_api.data.payloads.repository.ProjectRepository;
import com.afourathon.project_management_rest_api.data.payloads.request.MailingListRequest;
import com.afourathon.project_management_rest_api.service.JpaMailingListService;
import com.afourathon.project_management_rest_api.service.JpaProjectService;
import com.afourathon.project_management_rest_api.service.MailingListService;
import com.afourathon.project_management_rest_api.service.ProjectService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MailingListServiceTests {

	@InjectMocks
	private MailingListService mailingListService = new JpaMailingListService();

	@Mock
	private MailingListRepository mailingListRepository;

	@InjectMocks
	private ProjectService projectService = new JpaProjectService();

	@Mock
	private ProjectRepository projectRepository;

	@Test
	public void findEmailByIdTest() {
		Long mailId = 101L;

		Optional<MailingList> objMailingList = Optional.of(new MailingList(mailId, "John Smith", "john.smith@myorg.com"));

		when(mailingListRepository.findById(mailId)).thenReturn(objMailingList);

		assertTrue(mailingListService.findEmailById(mailId).isPresent());
	}

	@Test
	public void findEmailByRecipientNameTest() {
		String recipientName = "John Smith";

		when(mailingListRepository.findByRecipientName(recipientName)).thenReturn(
				Stream.of(new MailingList(1L, "John Smith", "john.smith@myorg.com"))
				.collect(Collectors.toList()));

		assertEquals(1, mailingListService.findEmailByRecipientName(recipientName).size());
	}

	@Test
	public void findAllEmailsTest() {
		String field = "recipientName";
		when(mailingListRepository.findAll(Sort.by(Sort.Direction.ASC, field))).thenReturn(
				Stream.of(new MailingList(1L, "John Smith", "john.smith@myorg.com"),
						new MailingList(2L, "Foo Bar", "foo.bar@myorg.com"))
				.collect(Collectors.toList()));

		assertEquals(2, mailingListService.findAllEmails().size());
	}

	@Test
	public void updateEmailTest() {
		Long mailId = 101L;
		String updatedRecipientName = "Johnny Smith";
		String updatedMEmail = "johnny.smith@myinternationalorg.com";

		MailingList originalEmail = new MailingList(mailId, "John Smith", "john.smith@myorg.com");

		MailingListRequest mailingListRequest = new MailingListRequest(updatedRecipientName, updatedMEmail);

		MailingList updatedEmail = originalEmail;
		updatedEmail.setRecipientName(mailingListRequest.getRecipientName());
		updatedEmail.setEmail(mailingListRequest.getEmail());

		when(mock(MailingListService.class).updateEmail(mailId, mailingListRequest)).thenReturn(updatedEmail).getMock();

		assertTrue(updatedEmail.getRecipientName().equalsIgnoreCase(updatedRecipientName));
		assertTrue(updatedEmail.getEmail().equalsIgnoreCase(updatedMEmail));

	}

	@Test
	public void deleteEmailTest() {
		// Testing Project
		Long projectId = 1001L;
		Project project = new Project();
		project.setId(projectId);
		project.setName("Test Spring Boot Project");
		project.setStartDate(LocalDate.parse(new String("2022-01-01")));
		project.setEndDate(LocalDate.parse(new String("2022-12-31")));
		project.setManagerName("John Smith");
		project.setManagerEmail("john.smith@myorg.com");
		project.setMailingList(new HashSet<>());

		Optional<Project> objProject = Optional.of(project);

		when(projectRepository.findById(projectId)).thenReturn(objProject);

		assertTrue(projectService.findProjectById(projectId).isPresent());
		
		// Testing Mailing List
		Long mailId = 101L;
		MailingList email = new MailingList(mailId, "John Smith", "john.smith@myorg.com");
		
		Optional<MailingList> objExistingEmail = Optional.of(email);

		when(mailingListRepository.findById(mailId)).thenReturn(objExistingEmail);

		assertTrue(mailingListService.findEmailById(mailId).isPresent());
		
		MailingList emailToBeDeleted = null;
		
		if(objExistingEmail.isPresent())
			emailToBeDeleted = objExistingEmail.get();

		if(objProject.isPresent()) {
			Project existingProject = objProject.get();
			
			Set<MailingList> existingMailingList = existingProject.getMailingList();
			
			if(!existingMailingList.contains(emailToBeDeleted))
				mailingListRepository.delete(emailToBeDeleted);
		}
	}
	
	@Test
	public void deleteAllEmailsTest() {
		mailingListService.deleteAllEmails();

		verify(mailingListRepository, times(1)).deleteAll();;
	}

}
