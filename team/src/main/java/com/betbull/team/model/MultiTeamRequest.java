package com.betbull.team.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class MultiTeamRequest {
	
	private List<TeamRequestDto> teamList;
	
	public MultiTeamRequest() {
		
	}

}
