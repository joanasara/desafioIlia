package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.PointEntities;

@Repository
public interface PointRepository extends JpaRepository<PointEntities, Integer >{
	
	List<PointEntities> findByDateInterval(String initialDate, String finalDate);

}
