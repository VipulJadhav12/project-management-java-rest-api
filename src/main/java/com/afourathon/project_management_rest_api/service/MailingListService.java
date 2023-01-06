package com.afourathon.project_management_rest_api.service;

import java.util.List;
import java.util.Optional;

import com.afourathon.project_management_rest_api.data.entity.MailingList;
import com.afourathon.project_management_rest_api.data.payloads.request.MailingListRequest;

public interface MailingListService {
	
	public boolean checkIfEmailExistsById(Long mailId);
	
	/* ========================================
	 * List of service fns to FIND Emails
	 * based on conditions.
	 * ========================================
	 */
	public Optional<MailingList> findEmailById(Long mailId);
	
	public List<MailingList> findEmailByRecipientName(String recipientName);
	
	public List<MailingList> findAllEmails();
	
	public List<MailingList> findAllUnAssignedEmailsByProjectId(Long projectId);

	/* ========================================
	 * List of service fns to CREATE and 
	 * UPDATE Emails.
	 * ========================================
	 */
	public MailingList addEmail(MailingListRequest mailingListRequest);
	
	public MailingList updateEmail(Long mailId, MailingListRequest mailingListRequest);
	
	/* ========================================
	 * List of service fns to DELETE Email(s)
	 * ========================================
	 */
	public boolean deleteEmailById(Long mailId);
	
	public boolean deleteEmailByIdAndProjectId(Long projectId, Long mailId);
	
	public boolean deleteAllEmails();

}
