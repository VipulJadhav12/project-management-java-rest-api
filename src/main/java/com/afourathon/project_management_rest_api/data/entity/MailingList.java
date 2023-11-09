package com.afourathon.project_management_rest_api.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "mailing_list", 
uniqueConstraints = { @UniqueConstraint(columnNames = { "recipient_name", "email" }) })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MailingList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mail_id")
	private Long id;
	
	@Column(name = "recipient_name")
	private String recipientName;
	
	private String email;
	
	public MailingList(String recipientName, String email) {
		super();
		this.recipientName = recipientName;
		this.email = email;
	}
	
}
