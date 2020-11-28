package com.betbull.team.model;

import java.util.List;

import com.betbull.team.model.base.BaseTeamResponse;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MultiTeamResponse extends BaseTeamResponse {

	private List<Team> teamList;

	public MultiTeamResponse() {
		
	}
	
	public MultiTeamResponse(int code, String desc) {
		super(code, desc);
	}
	
	public MultiTeamResponse(int code, String desc, List<Team> teamList) {
		super(code, desc);
		this.teamList = teamList;
	}
}
