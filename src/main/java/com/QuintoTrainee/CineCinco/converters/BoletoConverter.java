package com.QuintoTrainee.CineCinco.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuintoTrainee.CineCinco.entities.Boleto;
import com.QuintoTrainee.CineCinco.entities.Butaca;
import com.QuintoTrainee.CineCinco.entities.Funcion;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.BoletoModel;
import com.QuintoTrainee.CineCinco.repositories.BoletoRepository;
import com.QuintoTrainee.CineCinco.repositories.ButacaRepository;
import com.QuintoTrainee.CineCinco.repositories.FuncionRepository;

import lombok.RequiredArgsConstructor;

@Component("BoletoConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BoletoConverter extends Converter<BoletoModel, Boleto> {

	private final BoletoRepository boletoRepository;
	private final ButacaRepository butacaRepository;
	private final FuncionRepository funcionRepository;
	private final ButacaConverter butacaConverter;
	private final FuncionConverter funcionConverter;

	public Boleto modelToEntity(BoletoModel model) throws WebException {

		Boleto entity;

		if (model.getId() != null && !model.getId().isEmpty()) {
			entity = boletoRepository.getOne(model.getId());
		} else {
			entity = new Boleto();
		}

		try {

			Butaca entityButaca = null;
			if (model.getButaca().getId() != null) {
				entityButaca = butacaRepository.getOne(model.getButaca().getId());
			}
			entity.setButaca(entityButaca);

			Funcion entityFuncion = null;
			if (model.getFuncion().getId() != null) {
				entityFuncion = funcionRepository.getOne(model.getFuncion().getId());
			}
			entity.setFuncion(entityFuncion);

			BeanUtils.copyProperties(model, entity);

		} catch (Exception e) {
			throw new WebException("error al convertir el modelo " + model.toString() + " a entidad");
		}
		return entity;
	}

	public BoletoModel entityToModel(Boleto entity) throws WebException {
		BoletoModel model = new BoletoModel();
		try {

			if (entity.getButaca() != null) {
				model.setIdButaca(entity.getButaca().getId());
				model.setButaca(butacaConverter.entityToModel(butacaRepository.getOne(entity.getButaca().getId())));
			}

			if (entity.getFuncion() != null) {
				model.setIdFuncion(entity.getFuncion().getId());
				model.setFuncion(funcionConverter.entityToModel(funcionRepository.getOne(entity.getFuncion().getId())));
			}

			BeanUtils.copyProperties(entity, model);

		} catch (Exception e) {
			throw new WebException("Error al convertir la entidad " + entity.toString() + " a modelo");
		}
		return model;
	}

	public List<Boleto> modelsToEntities(List<BoletoModel> models) throws WebException {
		List<Boleto> entities = new ArrayList<>();
		for (BoletoModel model : models) {
			entities.add(modelToEntity(model));
		}
		return entities;
	}

	public List<BoletoModel> entitiesToModels(List<Boleto> entities) throws WebException {
		List<BoletoModel> models = new ArrayList<>();
		for (Boleto entity : entities) {
			models.add(entityToModel(entity));
		}
		return models;
	}

}
