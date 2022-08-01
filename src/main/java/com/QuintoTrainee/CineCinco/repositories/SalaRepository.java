package com.QuintoTrainee.CineCinco.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.QuintoTrainee.CineCinco.entities.Pelicula;
import com.QuintoTrainee.CineCinco.entities.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, String> {

	@Query("SELECT s from Sala s WHERE s.baja IS NULL")
	public List<Sala> listarSalasActivas();

}
