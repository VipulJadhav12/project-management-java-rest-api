package com.afourathon.project_management_rest_api.data.payloads.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailingListRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Recipient name cannot be null.")
	private String recipientName;
	
	@NotBlank(message = "Email cannot be null.")
	private String email;

}
