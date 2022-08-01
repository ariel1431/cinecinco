package com.QuintoTrainee.CineCinco.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.QuintoTrainee.CineCinco.entities.Compra;
import com.QuintoTrainee.CineCinco.entities.Pelicula;
import com.QuintoTrainee.CineCinco.models.CompraModel;

@Repository
public interface CompraRepository extends JpaRepository<Compra, String>{

	@Query("SELECT c FROM Compra c WHERE c.fechaAprobacionPago IS NULL")
	public List<Compra> getComprasBasura();

}
