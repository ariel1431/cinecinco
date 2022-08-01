package com.QuintoTrainee.CineCinco.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuintoTrainee.CineCinco.entities.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto,String>{

}
