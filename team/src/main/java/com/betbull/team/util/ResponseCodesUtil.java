package com.betbull.team.util;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResponseCodesUtil {

	public static Integer SUCCESS = 0;
	public static Integer ALREADY_EXISTS = 800;
	public static Integer ALREADY_ACTIVE = 801;
	public static Integer ALREADY_PASSIVE = 802;
	public static Integer NOT_EXISTS = 803;
	public static Integer COMMON_UNKNOWN_ERROR = 900;
	
	private static Map<Integer, String> responseCodesMap = Stream.of(
			new AbstractMap.SimpleEntry<>(SUCCESS, "SUCCESS"),
			new AbstractMap.SimpleEntry<>(ALREADY_EXISTS, "TEAM_ALREADY_EXISTS"), 
			  new AbstractMap.SimpleEntry<>(ALREADY_ACTIVE, "TEAM_ALREADY_ACTIVE"), 
			  new AbstractMap.SimpleEntry<>(ALREADY_PASSIVE, "TEAM_ALREADY_PASSIVE"),
			  new AbstractMap.SimpleEntry<>(NOT_EXISTS, "TEAM_NOT_EXISTS"),
			  new AbstractMap.SimpleEntry<>(COMMON_UNKNOWN_ERROR, "COMMON_UNKNOWN_ERROR"))
			  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	
	public static String getDescriptionByCode(Integer code) {
		return responseCodesMap.get(code);
	}


}
