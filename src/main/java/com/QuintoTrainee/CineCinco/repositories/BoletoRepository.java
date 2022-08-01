package com.QuintoTrainee.CineCinco.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.QuintoTrainee.CineCinco.entities.Boleto;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto, String>{
	
//	@Query("SELECT c FROM Boleto c WHERE c.usuario.id = :id")
//	public Boleto buscarBoletosPorUsuario(@Param("id") String id);

	@Query("SELECT b FROM Boleto b WHERE b.id = :id")
	public Boleto buscarBoletosPorId(@Param("id") String id);
}
