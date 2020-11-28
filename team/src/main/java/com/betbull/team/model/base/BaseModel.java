package com.betbull.team.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@MappedSuperclass
public abstract class BaseModel {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "record_date")
	private Date recordDate;
	
	@Column(name = "is_valid")
	private Boolean isValid;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "version")
	private Integer version;
	
	public BaseModel() {
		
	}
	
	public BaseModel (String name) {
		this.recordDate = new Date();
		this.isValid = Boolean.TRUE;
		this.lastModifiedDate = new Date();
		this.name = name;
		this.version = 1;
	}
}
