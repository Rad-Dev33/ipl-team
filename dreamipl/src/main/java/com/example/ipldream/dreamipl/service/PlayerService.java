package com.example.ipldream.dreamipl.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ipldream.dreamipl.db1.entity.Player;
import com.example.ipldream.dreamipl.db1.repository.PlayerRepositoryDB1;
import com.example.ipldream.dreamipl.db2.entity.Players;
import com.example.ipldream.dreamipl.db2.repository.PlayerRepositoryDB2;
import com.example.ipldream.dreamipl.db3.entity.Team_match;
import com.example.ipldream.dreamipl.db3.repository.TeamRepo;
import com.example.ipldream.dreamipl.dto.PlayerDto;

@Service
public class PlayerService {
	
	private final PlayerRepositoryDB1 db1;
	private final PlayerRepositoryDB2 db2;
	private final TeamRepo t1;
	
	public PlayerService(PlayerRepositoryDB1 playerRepositoryDB1, PlayerRepositoryDB2 playerRepositoryDB2,TeamRepo t1) {
        this.db1 = playerRepositoryDB1;
        this.db2 = playerRepositoryDB2;
        this.t1=t1;
    }
	
	
	public List<Team_match> getTodaysMatch(LocalDate date) {
	
		List<Team_match> teams=t1.findByMatchdate(date);
		return teams.isEmpty()?null:teams;
	}
	
	public List<Player> getAllPlayersByFeatureBatter(){
		return db1.findAll();
	}
	
	public List<Players> getAllPlayersByFeatureBowlers(){
		return db2.findAll();
	}
	
	public <T> Set<String> getPlayerPropertyNames(Class<T> cname) {
	    return Arrays.stream(cname.getDeclaredFields())  // Get all fields of Player
	                 .map(Field::getName)  // Extract field names
	                 .collect(Collectors.toSet());  // Convert to Set
	}
	

	
	 public List<PlayerDto> getPlayersFromBothDBs(String team) {
	        List<PlayerDto> players = new ArrayList<>();
	        players.addAll(convertToDTO(db1.findByTeam(team)));
	        players.addAll(convertToDTO(db2.findByTeam(team)));
	        return players;
	    }
	 
	 
	 public List<String> getAllTeams() {
	        List<String> teams = new ArrayList<>();
	        teams.addAll(db1.findAllTeam());
	        teams.addAll(db2.findAllTeams());
	        return teams.stream().distinct().collect(Collectors.toList());
	    }
	 
	 private List<PlayerDto> convertToDTO(List<?> players) {
	       return players.stream().map(player->{
	    	   
	    	   if(player instanceof Player) {
	    		   Player p=(Player) player;
	    		   return new PlayerDto(p.getId(), p.getPlayerName(),p.getTeam(),"Batter",p.getPoints(),p.getPointsPerMatch(),p.getWeightedppm(),p.getAdjustedppm(),p.getExperienceBoostedppm(),p.getTotalSum());
	    	   }
	    	   else {
	    		   Players p2=(Players) player;
	    		   return new PlayerDto(p2.getId(),p2.getPlayerName(),p2.getTeam(),"Bower",p2.getPoints(),p2.getPointsPerMatch(),p2.getWeightedppm(),p2.getAdjustedppm(),p2.getExperienceBoostedppm(),p2.getTotalSum());
	    	   }
	    	   
	       }).collect(Collectors.toList()); 
	 }

}
