package com.example.ipldream.dreamipl.controller;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ipldream.dreamipl.db1.entity.Player;
import com.example.ipldream.dreamipl.db2.entity.Players;
import com.example.ipldream.dreamipl.db3.entity.Team_match;
import com.example.ipldream.dreamipl.dto.PlayerDto;
import com.example.ipldream.dreamipl.service.PlayerService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class PlayerController {
	
	private final PlayerService playerService;
	
	
	//this is a constructor injection we can also use field injection
	public PlayerController(PlayerService playerService) {
		this.playerService=playerService;
		
	}
	
	
	 @GetMapping("/matches")
	public ResponseEntity<List<Team_match>> getTodaysMatch(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		
		 if(date==null) {
			 date=LocalDate.now();
		 }
		List<Team_match> matches=playerService.getTodaysMatch(date);
		return matches!=null?ResponseEntity.ok(matches):ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	 
	 
	  
	  @GetMapping("/players/fields")
	  public ResponseEntity<Set<String>> getAvailableFields() {
	      return ResponseEntity.ok(playerService.getPlayerPropertyNames(Player.class));
	  }
	  
	  
	  @GetMapping("players/bowling/fields")
	  public ResponseEntity<Set<String>> getAvailableFieldsBowling(){
		  return ResponseEntity.ok(playerService.getPlayerPropertyNames(Players.class));
	  }
	  
	  
	  
	   
	  @GetMapping("/players/dynamic")
	    public ResponseEntity<List<Map<String, Object>>> getPlayersDynamic(
	            @RequestParam(value = "fields", required = false) List<String> fields) {

		// Create a final copy of the fields list
		    final List<String> finalFields = processFields(fields,Player.class);
		    
		    List<Player> players = playerService.getAllPlayersByFeatureBatter();
		    
		    List<Map<String, Object>> response = players.stream()
		        .map(player -> convertEntityToMap(player, finalFields))
		        .collect(Collectors.toList());
		    
		    return ResponseEntity.ok(response);
	    }
	  
	  
	  @GetMapping("/players/bowing/dynamic")
	    public ResponseEntity<List<Map<String, Object>>> getPlayersDynamicBowing(
	            @RequestParam(value = "fields", required = false) List<String> fields) {

		// Create a final copy of the fields list
		    final List<String> finalFields = processFields(fields,Players.class);
		    
		    List<Players> players = playerService.getAllPlayersByFeatureBowlers();
		    
		    List<Map<String, Object>> response = players.stream()
		        .map(player -> convertEntityToMap(player, finalFields))
		        .collect(Collectors.toList());
		    
		    return ResponseEntity.ok(response);
	    }
	
	
	  
	  private List<String> processFields(List<String> originalFields,Class<?> clazz) {
		  
		    Set<String> allowedFields =playerService.getPlayerPropertyNames(clazz);
		    
		  
		    // Create a new list instead of modifying the original
		    return originalFields.stream()
		        .filter(allowedFields::contains)
		        .collect(Collectors.toList());
		}
	  
	  private <T> Map<String, Object> convertEntityToMap(T entity, List<String> fields) {
		    BeanWrapperImpl wrapper = new BeanWrapperImpl(entity);
		    Map<String, Object> map = new LinkedHashMap<>();
		    fields.forEach(field -> 
		        map.put(field, wrapper.getPropertyValue(field))
		    );
		    return map;
		}
	
	
	@GetMapping("/teams")
	public ResponseEntity<List<String>> getAllTeams(){
		
		List<String> teams=playerService.getAllTeams();
		if (teams.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
		return ResponseEntity.ok(teams);
	}
	
	
	@GetMapping("/players")
    public ResponseEntity<Map<String, List<PlayerDto>>> getPlayersByTeams(@RequestParam String team1,@RequestParam String team2) {
        Map<String, List<PlayerDto>> response = new HashMap<>();
        response.put("team1", playerService.getPlayersFromBothDBs(team1));
        response.put("team2", playerService.getPlayersFromBothDBs(team2));
        return ResponseEntity.ok(response);
    }
	

}
