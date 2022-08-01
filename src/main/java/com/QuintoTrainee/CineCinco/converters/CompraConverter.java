package com.QuintoTrainee.CineCinco.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuintoTrainee.CineCinco.entities.Boleto;
import com.QuintoTrainee.CineCinco.entities.Compra;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.BoletoModel;
import com.QuintoTrainee.CineCinco.models.CompraModel;
import com.QuintoTrainee.CineCinco.repositories.CompraRepository;

import lombok.RequiredArgsConstructor;

@Component("CompraConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompraConverter extends Converter<CompraModel, Compra> {

	private final CompraRepository compraRepository;
	private final BoletoConverter boletoConverter;

	public Compra modelToEntity(CompraModel model) throws WebException {

		Compra entity;

		if (model.getId() != null && !model.getId().isEmpty()) {
			entity = compraRepository.getOne(model.getId());
		} else {
			entity = new Compra();
		}

		try {

			List<Boleto> entityBoletos = new ArrayList<>();

			if (model.getBoletos() != null) {
				entityBoletos = boletoConverter.modelsToEntities(model.getBoletos());
			}

			entity.setBoletos(entityBoletos);

			BeanUtils.copyProperties(model, entity);

		} catch (Exception e) {
			throw new WebException("Error al convertir el modelo " + entity.toString() + " a entidad");
		}

		return entity;
	}

	public CompraModel entityToModel(Compra entity) throws WebException {

		CompraModel model = new CompraModel();

		try {

			List<BoletoModel> modelBoletos = new ArrayList<>();
			List<String> idsBoletos = new ArrayList<>();

			if (entity.getBoletos() != null) {
				modelBoletos = boletoConverter.entitiesToModels(entity.getBoletos());
			}

			for (BoletoModel boletoModel : modelBoletos) {
				idsBoletos.add(boletoModel.getId());
			}

			model.setBoletos(modelBoletos);
			model.setIdsBoletos(idsBoletos);

			BeanUtils.copyProperties(entity, model);

		} catch (Exception e) {
			throw new WebException("Error al convertir la entidad " + entity.toString() + " a modelo");
		}

		return model;
	}

	public List<Compra> modelsToEntities(List<CompraModel> models) throws WebException {

		List<Compra> entities = new ArrayList<>();

		for (CompraModel model : models) {
			entities.add(modelToEntity(model));
		}

		return entities;
	}

	public List<CompraModel> entitiesToModels(List<Compra> entities) throws WebException {

		List<CompraModel> models = new ArrayList<>();

		for (Compra entity : entities) {
			models.add(entityToModel(entity));
		}

		return models;
	}

}
