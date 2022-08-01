package com.QuintoTrainee.CineCinco.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.QuintoTrainee.CineCinco.entities.Funcion;
import com.QuintoTrainee.CineCinco.entities.Pelicula;
import com.QuintoTrainee.CineCinco.entities.Sala;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion, String>{

	@Query("SELECT f from Funcion f WHERE f.baja IS NULL")
	public List<Funcion> listarFuncionesActivas();

	@Query("SELECT f from Funcion f WHERE f.pelicula = :pelicula AND f.baja IS NULL")
	public List<Funcion> listarFuncionesActivasPorPelicula(@Param("pelicula") Pelicula pelicula);
}
