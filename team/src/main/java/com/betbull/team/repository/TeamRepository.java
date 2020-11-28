package com.betbull.team.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.betbull.team.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	
	List<Team> findByNameContainingIgnoreCase(String name);
	List<Team> findByIsValid(Boolean isValid);
	Boolean existsByNameAndNation(String name, String nation);
	@Transactional
	void deleteByIdIn(List<Long> id);
	@Transactional
	@Modifying
	@Query("update Team t set t.isValid=:isValid where t.id=:id")
	void changeValidStatusOfTeam(@Param(value = "id") Long id, @Param(value = "isValid") Boolean isValid);

}
