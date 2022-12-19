package com.afourathon.project_management_rest_api.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "weekly_status")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WeeklyStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String status;
	
	private String highlight;
	
	private String risk;
	
	@Column(name = "weekly_end_date")
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate weeklyEndDate;
	
	@Column(name = "created_on")
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime createdOn;
	
	@Column(name = "last_modified_on")
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime lastModifiedOn;
	
	public WeeklyStatus(String status, String highlight, String risk, LocalDate weeklyEndDate, LocalDateTime createdOn, LocalDateTime lastModifiedOn) {
		super();
		this.status = status;
		this.highlight = highlight;
		this.risk = risk;
		this.weeklyEndDate = weeklyEndDate;
		this.createdOn = createdOn;
		this.lastModifiedOn = lastModifiedOn;
	}
	
}