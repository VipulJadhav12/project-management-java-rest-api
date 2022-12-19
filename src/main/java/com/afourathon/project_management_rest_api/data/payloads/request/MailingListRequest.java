package com.afourathon.project_management_rest_api.data.payloads.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailingListRequest {
	
	@NotBlank(message = "Recipient name cannot be null.")
	private String recipientName;
	
	@NotBlank(message = "Email cannot be null.")
	private String email;

}
