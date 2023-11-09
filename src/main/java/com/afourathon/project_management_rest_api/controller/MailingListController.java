package com.afourathon.project_management_rest_api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afourathon.project_management_rest_api.configuration.Configurations;
import com.afourathon.project_management_rest_api.data.entity.MailingList;
import com.afourathon.project_management_rest_api.data.payloads.request.MailingListRequest;
import com.afourathon.project_management_rest_api.data.payloads.response.ApiResponse;
import com.afourathon.project_management_rest_api.exception.EmailNotFoundException;
import com.afourathon.project_management_rest_api.exception.ProjectNotFoundException;
import com.afourathon.project_management_rest_api.service.MailingListService;
import com.afourathon.project_management_rest_api.service.ProjectService;

@RestController
@RequestMapping("/api/v1/emails")
public class MailingListController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	MailingListService mailingListService;
	
	@Autowired
	Configurations configurations;
	
	@GetMapping(value = {"", "/"})
	public ResponseEntity<String> defaultHealthCheck() {
		return new ResponseEntity<String>("Default HTTP Status: OK", HttpStatus.OK);
	}
	
	@GetMapping("/getBy=ID/email/{mailId}")
	public ResponseEntity<MailingList> getEmailById(@PathVariable Long mailId) throws EmailNotFoundException {
		if(!mailingListService.checkIfEmailExistsById(mailId)) {
			throw new EmailNotFoundException(String.format(Configurations.INVALID_EMAIL_ID, mailId));
		}
		
		Optional<MailingList> objEmail = mailingListService.findEmailById(mailId);
		
		if(!objEmail.isPresent())
			throw new EmailNotFoundException(String.format(Configurations.INVALID_EMAIL_ID, mailId));
		
		return ResponseEntity.ok(objEmail.get());
	}
	
	@GetMapping("/getBy=RECIPIENT_NAME/recipient_name/{recipientName}")
	public ResponseEntity<List<MailingList>> getEmailByRecipientName(@PathVariable String recipientName) throws EmailNotFoundException {
		List<MailingList> mailingList = mailingListService.findEmailByRecipientName(recipientName);
		
		if(null == mailingList || 0 == mailingList.size())
			throw new EmailNotFoundException(String.format(Configurations.INVALID_EMAIL_RECIPIENT_NAME, recipientName));
		
		return ResponseEntity.ok(mailingList);
	}
	
	@GetMapping("/getAllBy=NONE")
	public ResponseEntity<List<MailingList>> getAllEmails() {
		List<MailingList> mailingList = mailingListService.findAllEmails();
		
		return ResponseEntity.ok(mailingList);
	}
	
	@GetMapping("/getAllUnAssignedBy=PROJECT_ID/project/{projectId}")
	public ResponseEntity<List<MailingList>> getAllUnAssignedEmails(@PathVariable Long projectId) throws ProjectNotFoundException {
		if(!projectService.checkIfProjectExistsById(projectId)) {
			throw new ProjectNotFoundException(String.format(Configurations.INVALID_PROJECT_ID, projectId));
		}
		
		List<MailingList> mailingList = mailingListService.findAllUnAssignedEmailsByProjectId(projectId);
		
		return ResponseEntity.ok(mailingList);
	}
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addEmail(@RequestBody @Valid MailingListRequest mailingListRequest) {
		MailingList email = mailingListService.addEmail(mailingListRequest);
		
		ApiResponse apiResponse = null;
		if(null == email) {
			apiResponse = new ApiResponse(String.format(Configurations.EMAIL_ADD_ON_FAILED), HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		apiResponse = new ApiResponse(String.format(Configurations.EMAIL_ADD_ON_SUCCESS, email.getId()), HttpStatus.CREATED);
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}
	
	@PutMapping("/updateBy=ID/email/{mailId}")
	public ResponseEntity<ApiResponse> updateEmail(@PathVariable Long mailId, @RequestBody @Valid MailingListRequest mailingListRequest) throws EmailNotFoundException {
		if(!mailingListService.checkIfEmailExistsById(mailId)) {
			throw new EmailNotFoundException(String.format(Configurations.INVALID_EMAIL_ID, mailId));
		}
		
		MailingList updatedEmail = mailingListService.updateEmail(mailId, mailingListRequest);
		
		ApiResponse apiResponse = null;
		if(null == updatedEmail) {
			apiResponse = new ApiResponse(String.format(Configurations.EMAIL_UPDATE_ON_FAILED, mailId), HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		apiResponse = new ApiResponse(String.format(Configurations.EMAIL_UPDATE_ON_SUCCESS, updatedEmail.getId()), HttpStatus.OK);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteBy=ID/email/{mailId}")
	public ResponseEntity<ApiResponse> deleteEmailById(@PathVariable Long mailId) throws EmailNotFoundException {
		if(!mailingListService.checkIfEmailExistsById(mailId)) {
			throw new EmailNotFoundException(String.format(Configurations.INVALID_EMAIL_ID, mailId));
		}
		
		ApiResponse apiResponse = null;
		boolean isDeleted = mailingListService.deleteEmailById(mailId);
		if(!isDeleted) {
			apiResponse = new ApiResponse(String.format(Configurations.DELETE_EMAIL_ON_FAILED, mailId), HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		apiResponse = new ApiResponse(String.format(Configurations.DELETE_EMAIL_ON_SUCCESS, mailId), HttpStatus.OK);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteAllBy=NONE")
	public ResponseEntity<ApiResponse> deleteAllEmails() {
		boolean isAllDeleted = mailingListService.deleteAllEmails();
		
		ApiResponse apiResponse = null;
		if(!isAllDeleted) {
			apiResponse = new ApiResponse(String.format(Configurations.DELETE_ALL_EMAILS_ON_FAILED), HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		apiResponse = new ApiResponse(String.format(Configurations.DELETE_ALL_EMAILS_ON_SUCCESS), HttpStatus.OK);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
