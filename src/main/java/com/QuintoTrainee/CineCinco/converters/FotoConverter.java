package com.QuintoTrainee.CineCinco.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuintoTrainee.CineCinco.entities.Foto;
import com.QuintoTrainee.CineCinco.entities.Pelicula;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.FotoModel;
import com.QuintoTrainee.CineCinco.models.PeliculaModel;
import com.QuintoTrainee.CineCinco.repositories.FotoRepository;


import lombok.RequiredArgsConstructor;
@Component("FotoConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FotoConverter extends Converter<FotoModel, Foto> {
    private final FotoRepository fotoRepository;
    
	@Override
	public Foto modelToEntity(FotoModel model) throws WebException {
		Foto entity;
		if (model.getId() != null && !model.getId().isEmpty()) {
			entity = fotoRepository.getOne(model.getId());
		} else {
			entity = new Foto();
		}

		try {
			BeanUtils.copyProperties(model, entity);
		} catch (Exception e) {
			throw new WebException("Error al convertir el modelo " + entity.toString() + " a entidad");
		}

		return entity;
	}

	@Override
	public FotoModel entityToModel(Foto entity) throws WebException {
		FotoModel model = new FotoModel();
		try {
			BeanUtils.copyProperties(entity, model);
		} catch (Exception e) {
			throw new WebException("Error al convertir la entidad " + entity.toString() + " a modelo");
		}

		return model;
	}

	@Override
	public List<Foto> modelsToEntities(List<FotoModel> models) throws WebException {
		List<Foto> entities = new ArrayList<>();

		for (FotoModel model : models) {
			entities.add(modelToEntity(model));
		}

		return entities;
	}

	@Override
	public List<FotoModel> entitiesToModels(List<Foto> entities) throws WebException {
		List<FotoModel> models = new ArrayList<>();

		for (Foto entity : entities) {
			models.add(entityToModel(entity));
		}

		return models;
	}

}
