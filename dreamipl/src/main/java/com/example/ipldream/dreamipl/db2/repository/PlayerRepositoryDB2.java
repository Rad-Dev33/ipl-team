package com.example.ipldream.dreamipl.db2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ipldream.dreamipl.db2.entity.Players;

@Repository
public interface PlayerRepositoryDB2 extends JpaRepository<Players, Integer>{
	
	List<Players> findByTeam(String Team);
    
    @Query("SELECT DISTINCT p.team FROM Players p")
    List<String> findAllTeams();
    
    List<Players> findAll();

}
