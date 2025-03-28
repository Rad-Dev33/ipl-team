package com.example.ipldream.dreamipl.db3.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "team_match")
@NoArgsConstructor
@AllArgsConstructor
public class Team_match {
	
	@Id
	private long id;
	String stadium;
	String city;
	

	LocalDate matchdate;
	LocalTime matchtime;
	String team1;
	String team2;
	

}
