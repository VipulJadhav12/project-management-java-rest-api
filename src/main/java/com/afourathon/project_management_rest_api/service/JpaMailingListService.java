package com.afourathon.project_management_rest_api.service;

import java.util.List;
import java.util.Optional;

import com.afourathon.project_management_rest_api.data.entity.MailingList;
import com.afourathon.project_management_rest_api.data.payloads.request.MailingListRequest;

public class JpaMailingListService implements MailingListService {

	@Override
	public boolean checkIfEmailExistsById(Long mailId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<MailingList> findEmailById(Long mailId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MailingList> findEmailByRecipientName(String recipientName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MailingList> findAllEmails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MailingList> findAllUnAssignedEmailsByProjectId(Long projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MailingList addEmail(MailingListRequest mailingListRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MailingList updateEmail(Long mailId, MailingListRequest mailingListRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteEmailById(Long mailId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEmailByIdAndProjectId(Long projectId, Long mailId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteAllEmails() {
		// TODO Auto-generated method stub
		return false;
	}

}
