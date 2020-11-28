package com.betbull.team.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.betbull.team.model.base.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@Entity
public class Currency extends BaseModel {
	
	@Column(name = "code")
	private String code;
	@Column(name = "symbol")
	private String symbol;
	
	public Currency() {
		
	}
	
	public Currency(String name, String code, String symbol) {
		super(name);
		this.code = code;
		this.symbol = symbol;
	}

}
