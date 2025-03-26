package com.example.ipldream.dreamipl.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {
	
	private int id;
	private String playerName;
	private String team;
	private String playerType;
	private double points;
	private double points_per_match;
	private double weighted_ppm;
	private double adjusted_ppm;
	private double experience_boosted_ppm;
    private double total_sum;

}
