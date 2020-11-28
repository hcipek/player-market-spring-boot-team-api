package com.betbull.team.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.betbull.team.model.base.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "team")
public class Team extends BaseModel{
	
	@Column(name = "nation")
	private String nation; 
	
	@Column(name = "established_year")
	private Integer establishedYear;
	
	@JoinColumn(name = "currency")
	@OneToOne
	private Currency currency;
	
	public Team() {
		
	}
	
	public Team(String name, Integer establishedYear, String nation, Currency currency) {
		super(name);
		this.establishedYear = establishedYear;
		this.nation = nation;
		this.currency = currency;
	}

}
