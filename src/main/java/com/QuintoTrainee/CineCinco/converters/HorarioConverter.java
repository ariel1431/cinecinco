package com.QuintoTrainee.CineCinco.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuintoTrainee.CineCinco.entities.Horario;
import com.QuintoTrainee.CineCinco.entities.Usuario;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.CompraModel;
import com.QuintoTrainee.CineCinco.models.HorarioModel;
import com.QuintoTrainee.CineCinco.models.UsuarioModel;
import com.QuintoTrainee.CineCinco.repositories.HorarioRepository;

import lombok.RequiredArgsConstructor;

@Component("HorarioConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HorarioConverter extends Converter<HorarioModel, Horario> {
	private final HorarioRepository horarioRepository;

	public Horario modelToEntity(HorarioModel model) throws WebException {
		Horario entity;

		if (model.getId() != null && !model.getId().isEmpty()) {
			entity = horarioRepository.getOne(model.getId());
		} else {
			entity = new Horario();
		}

		try {

			BeanUtils.copyProperties(model, entity);

		} catch (Exception e) {
			throw new WebException("Error al convertir el modelo " + entity.toString() + " a entidad");
		}

		return entity;
	}

	
	public HorarioModel entityToModel(Horario entity) throws WebException {

		HorarioModel model = new HorarioModel();

		try {

			BeanUtils.copyProperties(entity, model);

		} catch (Exception e) {
			throw new WebException("Error al convertir la entidad " + entity.toString() + " a modelo");
		}

		return model;
	}

	public List<Horario> modelsToEntities(List<HorarioModel> list) throws WebException {
		List<Horario> entities = new ArrayList<>();

		for (HorarioModel model : list) {
			entities.add(modelToEntity(model));
		}

		return entities;

	}

	public List<HorarioModel> entitiesToModels(List<Horario> entities) throws WebException {
		List<HorarioModel> models = new ArrayList<>();

		for (Horario entity : entities) {
			models.add(entityToModel(entity));
		}

		return models;
	}

}
