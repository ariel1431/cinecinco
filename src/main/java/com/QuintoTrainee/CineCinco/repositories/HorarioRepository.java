package com.QuintoTrainee.CineCinco.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuintoTrainee.CineCinco.entities.Horario;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, String> {

}
