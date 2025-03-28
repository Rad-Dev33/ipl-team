package com.example.ipldream.dreamipl.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ipldream.dreamipl.db1.entity.Player;


@Repository
public interface PlayerRepositoryDB1 extends JpaRepository<Player, Integer>{
	
	List<Player> findByTeam(String team);
    
    @Query("SELECT DISTINCT p.team FROM Player p")
    List<String> findAllTeam();
    
    List<Player> findAll();

}
