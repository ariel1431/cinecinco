package com.QuintoTrainee.CineCinco.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuintoTrainee.CineCinco.entities.Sala;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.SalaModel;
import com.QuintoTrainee.CineCinco.repositories.SalaRepository;

import lombok.RequiredArgsConstructor;

@Component("SalaConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SalaConverter extends Converter<SalaModel, Sala> {

	private final SalaRepository salaRepository;

	public Sala modelToEntity(SalaModel model) throws WebException {

		Sala entity;

		if (model.getId() != null && !model.getId().isEmpty()) {
			entity = salaRepository.getOne(model.getId());
		} else {
			entity = new Sala();
		}

		try {
			BeanUtils.copyProperties(model, entity);
		} catch (Exception e) {
			throw new WebException("Error al convertir el modelo " + entity.toString() + " a entidad");
		}

		return entity;
	}

	public SalaModel entityToModel(Sala entity) throws WebException {

		SalaModel model = new SalaModel();

		try {
			BeanUtils.copyProperties(entity, model);
		} catch (Exception e) {
			throw new WebException("Error al convertir la entidad " + entity.toString() + " a modelo");
		}

		return model;
	}

	public List<Sala> modelsToEntities(List<SalaModel> models) throws WebException {

		List<Sala> entities = new ArrayList<>();

		for (SalaModel model : models) {
			entities.add(modelToEntity(model));
		}

		return entities;
	}

	public List<SalaModel> entitiesToModels(List<Sala> entities) throws WebException {

		List<SalaModel> models = new ArrayList<>();

		for (Sala entity : entities) {
			models.add(entityToModel(entity));
		}

		return models;
	}

}
