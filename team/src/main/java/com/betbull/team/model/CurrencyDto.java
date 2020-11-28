package com.betbull.team.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CurrencyDto {
	
	private String name;
	private String code;
	private String symbol;
	
	public CurrencyDto () {
		
	}
}
