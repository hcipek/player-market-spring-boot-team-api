package com.betbull.team.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class TeamRequestDto{

	private String name;
	private Integer establishedYear;
	private String nation;
}
