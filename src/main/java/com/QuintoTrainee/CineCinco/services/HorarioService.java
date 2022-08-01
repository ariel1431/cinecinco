package com.QuintoTrainee.CineCinco.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuintoTrainee.CineCinco.converters.HorarioConverter;
import com.QuintoTrainee.CineCinco.entities.Funcion;
import com.QuintoTrainee.CineCinco.entities.Horario;
import com.QuintoTrainee.CineCinco.models.FuncionModel;
import com.QuintoTrainee.CineCinco.models.HorarioModel;
import com.QuintoTrainee.CineCinco.repositories.HorarioRepository;

@Service
public class HorarioService {

	@Autowired
	private HorarioRepository horarioRepository;
	@Autowired
	private HorarioConverter horarioConverter;
	
	public Horario guardar(HorarioModel horarioModel) {

		try {

			//validar(horarioModel);
			Horario horarioEntity = horarioConverter.modelToEntity(horarioModel);

			return horarioRepository.save(horarioEntity);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}
	
}
