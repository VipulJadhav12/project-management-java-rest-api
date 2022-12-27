package com.afourathon.project_management_rest_api.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.afourathon.project_management_rest_api.data.entity.MailingList;
import com.afourathon.project_management_rest_api.data.entity.Project;
import com.afourathon.project_management_rest_api.data.payloads.repository.MailingListRepository;
import com.afourathon.project_management_rest_api.data.payloads.repository.ProjectRepository;
import com.afourathon.project_management_rest_api.data.payloads.request.MailingListRequest;

@Service
public class JpaMailingListService implements MailingListService {

	@Autowired
	MailingListRepository mailingListRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Override
	public boolean checkIfEmailExistsById(Long mailId) {
		return mailingListRepository.existsById(mailId);
	}

	/* ========================================
	 * List of service fns to FIND Emails
	 * based on conditions.
	 * ========================================
	 */

	@Override
	public Optional<MailingList> findEmailById(Long mailId) {
		return mailingListRepository.findById(mailId);
	}

	@Override
	public List<MailingList> findEmailByRecipientName(String recipientName) {
		return mailingListRepository.findByRecipientName(recipientName);
	}

	@Override
	public List<MailingList> findAllEmails() {
		String field = "recipientName";
		return mailingListRepository.findAll(Sort.by(Sort.Direction.ASC, field));
	}

	@Override
	public List<MailingList> findAllUnAssignedEmailsByProjectId(Long projectId) {
		return mailingListRepository.findAllUnAssignedEmailsByProjectId(projectId);
	}

	/* ========================================
	 * List of service fns to CREATE and 
	 * UPDATE Emails.
	 * ========================================
	 */

	@Override
	public MailingList addEmail(MailingListRequest mailingListRequest) {
		MailingList email = new MailingList();
		email.setRecipientName(mailingListRequest.getRecipientName());
		email.setEmail(mailingListRequest.getEmail());

		try {
			return mailingListRepository.save(email);
		}
		catch(IllegalArgumentException ex) {
			return null;
		}
		catch(OptimisticLockingFailureException ex) {
			return null;
		}
	}

	@Override
	public MailingList updateEmail(Long mailId, MailingListRequest mailingListRequest) {
		Optional<MailingList> objExistingEmail = mailingListRepository.findById(mailId);

		if(objExistingEmail.isPresent()) {
			MailingList emailToBeUpdated = objExistingEmail.get();
			emailToBeUpdated.setRecipientName(mailingListRequest.getRecipientName());
			emailToBeUpdated.setEmail(mailingListRequest.getEmail());

			try {
				return mailingListRepository.save(emailToBeUpdated);
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
	public boolean deleteEmailById(Long mailId) {
		Optional<MailingList> objExistingEmail = mailingListRepository.findById(mailId);

		if(objExistingEmail.isPresent()) {
			MailingList emailToBeDeleted = objExistingEmail.get();

			try {
				mailingListRepository.delete(emailToBeDeleted);
			}
			catch(IllegalArgumentException ex) {
				return false;
			}
			catch(OptimisticLockingFailureException ex) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean deleteEmailByIdAndProjectId(Long projectId, Long mailId) {
		Optional<Project> objExistingProject = projectRepository.findById(projectId);
		Optional<MailingList> objExistingEmail = mailingListRepository.findById(mailId);

		if(objExistingProject.isPresent()) {
			Project existingProject = objExistingProject.get();

			if(objExistingEmail.isPresent()) {
				MailingList emailToBeDeleted = objExistingEmail.get();

				Set<MailingList> existingMailingList = existingProject.getMailingList();

				if(existingMailingList.contains(emailToBeDeleted))
					return false;

				try {
					mailingListRepository.delete(emailToBeDeleted);
				}
				catch(IllegalArgumentException ex) {
					return false;
				}
				catch(OptimisticLockingFailureException ex) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public boolean deleteAllEmails() {
		mailingListRepository.deleteAll();

		if(0 == mailingListRepository.count())
			return true;

		return false;
	}

}
