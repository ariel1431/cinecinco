package com.QuintoTrainee.CineCinco.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.QuintoTrainee.CineCinco.entities.Butaca;

@Repository
public interface ButacaRepository extends JpaRepository<Butaca,String>{
	
	@Query("SELECT c FROM Butaca c WHERE c.id = :id")
	public Butaca buscarButacaPorId(@Param("id") String id);

	
	

}
