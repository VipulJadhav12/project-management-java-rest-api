package com.afourathon.project_management_rest_api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
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
		MailingList mailingList = getMailingListDetails();
		Long mailId = mailingList.getId();

		Optional<MailingList> expectedOptMailingListObj = Optional.of(mailingList);

		when(mailingListRepository.findById(mailId)).thenReturn(expectedOptMailingListObj);

		Optional<MailingList> actualOptMailingListObj = mailingListService.findEmailById(mailId);
		
		MailingList expectedMailingList = expectedOptMailingListObj.get();
		MailingList actualMailingList = actualOptMailingListObj.get();
		
		assertTrue(actualOptMailingListObj.isPresent());
		assertEquals(expectedMailingList, actualMailingList);
		
		assertAll(
				() -> assertEquals(expectedMailingList.getId(), actualMailingList.getId()),
				() -> assertEquals(expectedMailingList.getRecipientName(), actualMailingList.getRecipientName()),
				() -> assertEquals(expectedMailingList.getEmail(), actualMailingList.getEmail())
		);
		
		verify(mailingListRepository, times(1)).findById(mailId);
	}

	@Test
	public void findEmailByRecipientNameTest() {
		MailingList mailingList = getMailingListDetails();
		String recipientName = mailingList.getRecipientName();

		when(mailingListRepository.findByRecipientName(recipientName)).thenReturn(
				Stream.of(mailingList)
				.collect(Collectors.toList()));
		
		List<MailingList> actualMailingList = mailingListService.findEmailByRecipientName(recipientName);

		assertEquals(1, actualMailingList.size());
		
		verify(mailingListRepository, atLeast(1)).findByRecipientName(recipientName);
	}

	@Test
	public void findAllEmailsTest() {
		String field = "recipientName";
		
		MailingList mailingList = getMailingListDetails();
		
		when(mailingListRepository.findAll(Sort.by(Sort.Direction.ASC, field))).thenReturn(
				Stream.of(mailingList)
				.collect(Collectors.toList()));
		
		List<MailingList> actualMailingList = mailingListService.findAllEmails();

		assertEquals(1, actualMailingList.size());
		
		verify(mailingListRepository, atLeast(1)).findAll(Sort.by(Sort.Direction.ASC, field));
	}
	
	@Test
	public void addEmailTest() {
		String recipientNameToBeAdded = "Johnny Smith";
		String emailToBeAdded = "johnny.smith@myinternationalorg.com";

		MailingListRequest mailingListRequest = new MailingListRequest(recipientNameToBeAdded, emailToBeAdded);
		
		MailingList expectedMailingList = getMailingListDetailsBasedOnMailingListRequest(mailingListRequest);
		
		when(mailingListRepository.save(any(MailingList.class))).thenReturn(expectedMailingList);
		
		MailingList actualMailingList = mailingListService.addEmail(mailingListRequest);

		assertEquals(expectedMailingList, actualMailingList);
		
		assertAll(
				() -> assertEquals(expectedMailingList.getId(), actualMailingList.getId()),
				() -> assertEquals(expectedMailingList.getRecipientName(), actualMailingList.getRecipientName()),
				() -> assertEquals(expectedMailingList.getEmail(), actualMailingList.getEmail())
		);
		
		verify(mailingListRepository, times(1)).save(any(MailingList.class));
	}

	@Test
	public void updateEmailTest() {
		String recipientNameToBeUpdated = "Johnny Smith";
		String emailToBeUpdated = "johnny.smith@myinternationalorg.com";

		MailingListRequest mailingListRequest = new MailingListRequest(recipientNameToBeUpdated, emailToBeUpdated);

		MailingList expectedMailingList = getMailingListDetailsBasedOnMailingListRequest(mailingListRequest);
		Long mailId = expectedMailingList.getId();

		Optional<MailingList> expectedOptMailingListObj =  Optional.of(expectedMailingList);
		
		when(mailingListRepository.findById(mailId)).thenReturn(expectedOptMailingListObj);
		when(mailingListRepository.save(any(MailingList.class))).thenReturn(expectedOptMailingListObj.get());

		MailingList actualMailingList = mailingListService.updateEmail(mailId, mailingListRequest);

		assertEquals(expectedMailingList, actualMailingList);
		assertTrue(actualMailingList.getRecipientName().equalsIgnoreCase(expectedMailingList.getRecipientName()));
		assertTrue(actualMailingList.getEmail().equalsIgnoreCase(expectedMailingList.getEmail()));
		
		verify(mailingListRepository, times(1)).findById(mailId);
		verify(mailingListRepository, times(1)).save(any(MailingList.class));
	}

	@Test
	public void deleteEmailByIdTest() {
		MailingList mailingList = getMailingListDetails();
		Long mailId = mailingList.getId();
		
		Optional<MailingList> expectedOptMailingListObj = Optional.of(mailingList);
		
		when(mailingListRepository.findById(mailId)).thenReturn(expectedOptMailingListObj);
		
		boolean actualResultOnDeletion = mailingListService.deleteEmailById(mailId);
		
		MailingList mailingListToBeDeleted = expectedOptMailingListObj.get();
		
		assertTrue(actualResultOnDeletion);
		
		verify(mailingListRepository, times(1)).findById(mailId);
		verify(mailingListRepository, times(1)).deleteEmailByProjects(mailId);
		verify(mailingListRepository, times(1)).delete(mailingListToBeDeleted);;
	}
	
	@Test
	public void deleteAllEmailsTest() {
		when(mailingListRepository.count()).thenReturn(0L);
		
		boolean actualResultOnDeletion = mailingListService.deleteAllEmails();
		
		assertTrue(actualResultOnDeletion);

		verify(mailingListRepository, times(1)).count();
		verify(mailingListRepository, times(1)).deleteAll();
	}
	
	private MailingList getMailingListDetails() {
		Long mailId = 101L;
		
		MailingList mailingList = 
				new MailingList(mailId, "John Smith", "john.smith@myorg.com");
		
		return mailingList;
	}
	
	private MailingList getMailingListDetailsBasedOnMailingListRequest(MailingListRequest request) {
		Long mailId = 101L;
		
		MailingList mailingList = new MailingList();
		mailingList.setId(mailId);
		mailingList.setRecipientName(request.getRecipientName());
		mailingList.setEmail(request.getEmail());
		
		return mailingList;
	}

}
