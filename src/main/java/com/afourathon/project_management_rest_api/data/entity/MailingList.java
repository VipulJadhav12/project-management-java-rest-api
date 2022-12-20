package com.afourathon.project_management_rest_api.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "mailing_list")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MailingList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "email_id")
	private Long id;
	
	@Column(name = "recipient_name")
	private String recipientName;
	
	private String email;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "mailingList", fetch = FetchType.LAZY)
	private Set<Project> projects = new HashSet<>();;

	public MailingList(String recipientName, String email) {
		super();
		this.recipientName = recipientName;
		this.email = email;
	}
	
}
