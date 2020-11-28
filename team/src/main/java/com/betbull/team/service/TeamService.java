package com.betbull.team.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.betbull.team.exception.TeamException;
import com.betbull.team.model.Currency;
import com.betbull.team.model.DefaultTeamResponse;
import com.betbull.team.model.MultiTeamRequest;
import com.betbull.team.model.MultiTeamResponse;
import com.betbull.team.model.Team;
import com.betbull.team.model.TeamRequestDto;
import com.betbull.team.repository.TeamRepository;
import com.betbull.team.util.ResponseCodesUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeamService {

	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public DefaultTeamResponse createTeam(MultiTeamRequest request) {
		log.debug("Create team started...");
		DefaultTeamResponse defaultTeamResponse = new DefaultTeamResponse();
		try {
			for(TeamRequestDto dto : request.getTeamList()) {
				if(teamRepository.existsByNameAndNation(dto.getName(), dto.getNation())) {
					log.error("{} already exists in {}", dto.getName(), dto.getNation());
					throw new TeamException(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.ALREADY_EXISTS), ResponseCodesUtil.ALREADY_EXISTS);
				}
				
				Currency currency = currencyService.getCurrencyByCountryName(dto.getNation());
				Team team = new Team(dto.getName(), dto.getEstablishedYear(), dto.getNation(), currency);
				team = teamRepository.save(team);
			}
			defaultTeamResponse = createResponseForSuccess();
		} catch (TeamException e) {
			defaultTeamResponse = createResponseForTeamException(e);
		} catch (Exception e) {
			defaultTeamResponse = createResponseForUnknownError(e);
		}
		return defaultTeamResponse;
	}
	
	public MultiTeamResponse getTeamByName(String name) {
		log.debug("Find team with name : {}", name);
		MultiTeamResponse multiTeamResponse = new MultiTeamResponse();
		try {
			List<Team> teamList = teamRepository.findByNameContainingIgnoreCase(name);
			if(CollectionUtils.isEmpty(teamList))
				throw new TeamException(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.NOT_EXISTS), ResponseCodesUtil.NOT_EXISTS);
			
			multiTeamResponse.setTeamList(teamList);
			multiTeamResponse.setResultCode(ResponseCodesUtil.SUCCESS);
			multiTeamResponse.setDescription(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.SUCCESS));
		} catch (TeamException e){
			multiTeamResponse.setResultCode(ResponseCodesUtil.NOT_EXISTS);
			multiTeamResponse.setDescription(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.NOT_EXISTS));
		} catch (Exception e){
			log.error("Something went wrong... exception message is {}", e.getMessage());
			multiTeamResponse.setResultCode(ResponseCodesUtil.COMMON_UNKNOWN_ERROR);
			multiTeamResponse.setDescription(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.COMMON_UNKNOWN_ERROR));
		}
		return multiTeamResponse;
	}
	
	public MultiTeamResponse getAllTeams() {
		log.debug("Find all teams...");
		MultiTeamResponse multiTeamResponse = new MultiTeamResponse();
		multiTeamResponse.setTeamList(teamRepository.findAll());
		multiTeamResponse.setResultCode(ResponseCodesUtil.SUCCESS);
		multiTeamResponse.setDescription(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.SUCCESS));
		return multiTeamResponse;
	}
	
	public MultiTeamResponse getAllActiveTeams() {
		log.debug("Find all active teams...");
		MultiTeamResponse multiTeamResponse = new MultiTeamResponse();
		multiTeamResponse.setTeamList(teamRepository.findByIsValid(Boolean.TRUE));
		multiTeamResponse.setResultCode(ResponseCodesUtil.SUCCESS);
		multiTeamResponse.setDescription(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.SUCCESS));
		return multiTeamResponse;
	}
	
	public MultiTeamResponse getAllPassiveTeams() {
		log.debug("Find all passive teams...");
		MultiTeamResponse multiTeamResponse = new MultiTeamResponse();
		multiTeamResponse.setTeamList(teamRepository.findByIsValid(Boolean.FALSE));
		multiTeamResponse.setResultCode(ResponseCodesUtil.SUCCESS);
		multiTeamResponse.setDescription(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.SUCCESS));
		return multiTeamResponse;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public DefaultTeamResponse activateTeamById(Long id){
		log.debug("Activating team with id : {}", id);
		DefaultTeamResponse defaultTeamResponse = new DefaultTeamResponse();
		try {
			if(isTeamValid(id)) 
				throw new TeamException(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.ALREADY_ACTIVE), ResponseCodesUtil.ALREADY_ACTIVE);
			
			teamRepository.changeValidStatusOfTeam(id, Boolean.TRUE);
			defaultTeamResponse = createResponseForSuccess();
		} catch(TeamException e) {
			defaultTeamResponse = createResponseForTeamException(e);
		}
		return defaultTeamResponse;
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
	public DefaultTeamResponse passivateTeamById(Long id){
		log.debug("Passivating team with id : {}", id);
		DefaultTeamResponse defaultTeamResponse;
		try {
			if(!isTeamValid(id)) 
				throw new TeamException(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.ALREADY_PASSIVE), ResponseCodesUtil.ALREADY_PASSIVE);
			
			teamRepository.changeValidStatusOfTeam(id, Boolean.FALSE);
			defaultTeamResponse = createResponseForSuccess();
		} catch(TeamException e) {
			defaultTeamResponse = createResponseForTeamException(e);
		}
		return defaultTeamResponse;
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
	public DefaultTeamResponse deleteTeamById(Long id){
		log.debug("Delete team with id : {}", id);
		DefaultTeamResponse defaultTeamResponse;
		try {
			if(!teamRepository.existsById(id))
				throw new TeamException(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.NOT_EXISTS), ResponseCodesUtil.NOT_EXISTS);

			teamRepository.deleteById(id);
			defaultTeamResponse = createResponseForSuccess();
		} catch(TeamException e) {
			defaultTeamResponse = createResponseForTeamException(e);
		}
		return defaultTeamResponse;
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
	public DefaultTeamResponse deleteAllPassiveTeams() {
		log.debug("Delete all passive teams...");
		DefaultTeamResponse defaultTeamResponse = new DefaultTeamResponse();
		try {
			List<Team> passiveTeamList = teamRepository.findByIsValid(Boolean.FALSE);
			
			if(!CollectionUtils.isEmpty(passiveTeamList))
				teamRepository.deleteByIdIn(passiveTeamList.stream().map(Team::getId).collect(Collectors.toList()));
			defaultTeamResponse = createResponseForSuccess();
		} catch (Exception e) {
			defaultTeamResponse = createResponseForUnknownError(e);
		}
		return defaultTeamResponse;
	}
	
	private DefaultTeamResponse createResponseForSuccess() {
		return createResponse(null, null);
	}
	
	private DefaultTeamResponse createResponseForTeamException(TeamException e) {
		return createResponse(e, null);
	}
	
	private DefaultTeamResponse createResponseForUnknownError(Exception e) {
		log.error("Something went wrong... exception message is {}", e.getMessage());
		return createResponse(null, e);
	}
	
	private DefaultTeamResponse createResponse(TeamException e1, Exception e2) {
		if(e1 != null)
			return new DefaultTeamResponse(e1.getErrorCode(), e1.getMessage());
		else if(e2 != null)
			return new DefaultTeamResponse(ResponseCodesUtil.COMMON_UNKNOWN_ERROR, ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.COMMON_UNKNOWN_ERROR));
		else
			return new DefaultTeamResponse(ResponseCodesUtil.SUCCESS, ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.SUCCESS));
			
	}
	
	private Boolean isTeamValid(Long id) {
		Optional<Team> optional = teamRepository.findById(id);
		if(optional.isEmpty()) {
			throw new TeamException(ResponseCodesUtil.getDescriptionByCode(ResponseCodesUtil.NOT_EXISTS), ResponseCodesUtil.NOT_EXISTS);
		} else {
			Team team = optional.get();
			return team.getIsValid();
		}
	}
}
