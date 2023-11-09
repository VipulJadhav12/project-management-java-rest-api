package com.afourathon.project_management_rest_api.data.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "project")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Long id;
	
	@Column(name = "project_name", unique = true)
	private String name;
	
	@Column(name = "start_date")
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	@Column(name = "manager_name")
	private String managerName;
	
	@Column(name = "manager_email")
	private String managerEmail;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "project_mailing_list", 
			joinColumns = {
					@JoinColumn(name = "project_id", referencedColumnName = "project_id")
			}, 
			inverseJoinColumns = {
					@JoinColumn(name = "mail_id", referencedColumnName = "mail_id")
			})
	private Set<MailingList> mailingList = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(targetEntity = WeeklyStatus.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "project_id", referencedColumnName = "project_id")
	private List<WeeklyStatus> weeklyStatuses;
	
	public Project(String name, LocalDate startDate, LocalDate endDate, String managerName, String managerEmail) {
		super();
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.managerName = managerName;
		this.managerEmail = managerEmail;
	}

	public Project(String name, LocalDate startDate, LocalDate endDate, String managerName, String managerEmail, Set<MailingList> mailingList) {
		super();
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.managerName = managerName;
		this.managerEmail = managerEmail;
		this.mailingList = mailingList;
	}

}
