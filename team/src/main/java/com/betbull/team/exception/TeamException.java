package com.betbull.team.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class TeamException extends RuntimeException{

	private static final long serialVersionUID = -8243137094929573252L;
	
	private String message;
	private int errorCode;
	
}
