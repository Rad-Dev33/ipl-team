package com.example.ipldream.dreamipl.db3.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ipldream.dreamipl.db3.entity.Team_match;

@Repository
public interface TeamRepo extends JpaRepository<Team_match, Long>{
	
	List<Team_match> findByMatchdate(LocalDate matchdate);
    


}
