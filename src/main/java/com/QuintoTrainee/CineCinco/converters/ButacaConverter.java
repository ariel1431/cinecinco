package com.QuintoTrainee.CineCinco.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuintoTrainee.CineCinco.entities.Butaca;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.ButacaModel;
import com.QuintoTrainee.CineCinco.repositories.ButacaRepository;

import lombok.RequiredArgsConstructor;

@Component("ButacaConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ButacaConverter extends Converter<ButacaModel, Butaca> {

	private final ButacaRepository butacaRepository;

	public Butaca modelToEntity(ButacaModel model) throws WebException {

		Butaca entity;

		if (model.getId() != null && !model.getId().isEmpty()) {
			entity = butacaRepository.getOne(model.getId());
		} else {
			entity = new Butaca();
		}

		try {
			BeanUtils.copyProperties(model, entity);
		} catch (Exception e) {
			throw new WebException("Error al convertir el modelo " + entity.toString() + " a entidad");
		}

		return entity;
	}

	public ButacaModel entityToModel(Butaca entity) throws WebException {

		ButacaModel model = new ButacaModel();

		try {
			BeanUtils.copyProperties(entity, model);
		} catch (Exception e) {
			throw new WebException("Error al convertir la entidad " + entity.toString() + " a modelo");
		}

		return model;
	}

	public List<Butaca> modelsToEntities(List<ButacaModel> models) throws WebException {

		List<Butaca> entities = new ArrayList<>();

		for (ButacaModel model : models) {
			entities.add(modelToEntity(model));
		}

		return entities;
	}

	public List<ButacaModel> entitiesToModels(List<Butaca> entities) throws WebException {

		List<ButacaModel> models = new ArrayList<>();

		for (Butaca entity : entities) {
			models.add(entityToModel(entity));
		}

		return models;
	}

}
