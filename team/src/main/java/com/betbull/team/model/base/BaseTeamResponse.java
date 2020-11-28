package com.betbull.team.model.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public abstract class BaseTeamResponse {
	
	private int resultCode;
    private String description;
	
	public BaseTeamResponse() {
		
	}

}
