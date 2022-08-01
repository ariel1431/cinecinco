package com.QuintoTrainee.CineCinco.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuintoTrainee.CineCinco.entities.Compra;
import com.QuintoTrainee.CineCinco.entities.Usuario;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.CompraModel;
import com.QuintoTrainee.CineCinco.models.UsuarioModel;
import com.QuintoTrainee.CineCinco.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component("UsuarioConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UsuarioConverter extends Converter<UsuarioModel, Usuario> {

	private final UsuarioRepository usuarioRepository;

	private final CompraConverter compraConverter;

	public Usuario modelToEntity(UsuarioModel model) throws WebException {

		Usuario entity;

		if (model.getId() != null && !model.getId().isEmpty()) {
			entity = usuarioRepository.getOne(model.getId());
		} else {
			entity = new Usuario();
		}

		try {

			List<Compra> entityCompras = new ArrayList<>();

			if (model.getCompras() != null) {
				entityCompras = compraConverter.modelsToEntities(model.getCompras());
			}

			entity.setCompras(entityCompras);

			BeanUtils.copyProperties(model, entity);

		} catch (Exception e) {
			throw new WebException("Error al convertir el modelo " + entity.toString() + " a entidad");
		}

		return entity;
	}

	public UsuarioModel entityToModel(Usuario entity) throws WebException {

		UsuarioModel model = new UsuarioModel();

		try {

			List<CompraModel> modelCompras = new ArrayList<>();
			List<String> idsCompras = new ArrayList<>();

			if (entity.getCompras() != null) {
				modelCompras = compraConverter.entitiesToModels(entity.getCompras());
			}

			for (CompraModel compraModel : modelCompras) {
				idsCompras.add(compraModel.getId());
			}

			model.setCompras(modelCompras);
			model.setIdsCompras(idsCompras);

			BeanUtils.copyProperties(entity, model);

		} catch (Exception e) {
			throw new WebException("Error al convertir la entidad " + entity.toString() + " a modelo");
		}

		return model;
	}

	public List<Usuario> modelsToEntities(List<UsuarioModel> models) throws WebException {

		List<Usuario> entities = new ArrayList<>();

		for (UsuarioModel model : models) {
			entities.add(modelToEntity(model));
		}

		return entities;
	}

	public List<UsuarioModel> entitiesToModels(List<Usuario> entities) throws WebException {

		List<UsuarioModel> models = new ArrayList<>();

		for (Usuario entity : entities) {
			models.add(entityToModel(entity));
		}

		return models;
	}

}
