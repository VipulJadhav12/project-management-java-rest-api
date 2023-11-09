package com.afourathon.project_management_rest_api.data.payloads.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.afourathon.project_management_rest_api.data.entity.MailingList;

public interface MailingListRepository extends JpaRepository<MailingList, Long> {
	
	public boolean existsById(Long id);

	public Optional<MailingList> findById(Long id);

	public List<MailingList> findByRecipientName(String recipient_name);
	
	@Query(
			  value = "SELECT * FROM mailing_list ml WHERE ml.mail_id NOT IN (SELECT mail_id FROM project_mailing_list WHERE project_mailing_list.project_id = ?1)"
			  		+ " ORDER BY ml.recipient_name", 
			  nativeQuery = true
		  )
	public List<MailingList> findAllUnAssignedEmailsByProjectId(Long projectId);
	
	@Modifying
	@Query(
			  value = "DELETE FROM project_mailing_list WHERE mail_id = ?1", 
			  nativeQuery = true
		  )
	public void deleteEmailByProjects(Long mailId);
}
