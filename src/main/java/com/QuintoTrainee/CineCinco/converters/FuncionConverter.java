package com.QuintoTrainee.CineCinco.converters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuintoTrainee.CineCinco.entities.Butaca;
import com.QuintoTrainee.CineCinco.entities.Funcion;
import com.QuintoTrainee.CineCinco.entities.Horario;
import com.QuintoTrainee.CineCinco.entities.Pelicula;
import com.QuintoTrainee.CineCinco.entities.Sala;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.ButacaModel;
import com.QuintoTrainee.CineCinco.models.FuncionModel;
import com.QuintoTrainee.CineCinco.models.HorarioModel;
import com.QuintoTrainee.CineCinco.repositories.FuncionRepository;
import com.QuintoTrainee.CineCinco.repositories.PeliculaRepository;
import com.QuintoTrainee.CineCinco.repositories.SalaRepository;
import com.QuintoTrainee.CineCinco.services.ButacaService;
import static com.QuintoTrainee.CineCinco.utils.Texts.*;

import lombok.RequiredArgsConstructor;

@Component("FuncionConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FuncionConverter extends Converter<FuncionModel, Funcion> {

	private final FuncionRepository funcionRepository;
	private final SalaRepository salaRepository;
	private final PeliculaRepository peliculaRepository;
	private final ButacaConverter butacaConverter;
	private final SalaConverter salaConverter;
	private final PeliculaConverter peliculaConverter;
	private final ButacaService butacaService;
	private final HorarioConverter horarioConverter;

	public Funcion modelToEntity(FuncionModel model) throws WebException {

		Funcion entity;

		System.out.println("FUNCION CONVERTER");

		if (model.getId() != null && !model.getId().isEmpty()) {
			System.out.println("-- obteniendo funcion del repositorio ID: " + model.getId());
			entity = funcionRepository.getOne(model.getId());
		} else {
			System.out.println("-- creando una funcion entidad nueva");
			entity = new Funcion();
		}

		try {

			System.out.println("BUTACAS");
			List<Butaca> entityButacas = new ArrayList<>();
			List<ButacaModel> modelsButacas = new ArrayList<>();
			Horario entityHorario = new Horario();
			if (model.getHorario() != null) {
				entityHorario = horarioConverter.modelToEntity(model.getHorario());
			}
			entity.setHorario(entityHorario);
			System.out.println("SALA");
			Sala entitySala = null;

			if (model.getIdSala() != null) {

				entitySala = salaRepository.getOne(model.getIdSala());
				System.out.println("-- convirtiendo la sala del model a entidad");

				// Como la funcion tiene asignada una sala se busca la cantidad de butacas de
				// esa sala para crear la matriz de butacas de la funcion
				if (model.getButacas() != null) {
					System.out.println("-- convirtiendo las butacas del model a entidades butacas");
					entityButacas = butacaConverter.modelsToEntities(model.getButacas());
				} else {

					int cantButacas = entitySala.getCantidadButacas();
					System.out.println("-- cantButacas = " + cantButacas);
					System.out.println("-- butacasPorFila = " + BUTACAS_POR_FILA);
					int cantidadFilas = (int) Math.floor(cantButacas / (double) BUTACAS_POR_FILA);
					System.out.println("-- cantidadFilas = " + cantidadFilas);
					int cantButacasUltimaFila = cantButacas - (BUTACAS_POR_FILA * cantidadFilas);
					System.out.println("-- cantButacasUltimaFila = " + cantButacasUltimaFila);

					char[] letras = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
							'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
					String nombreButaca;

					for (int i = 0; i < (cantidadFilas); i++) {
						for (int j = 0; j < BUTACAS_POR_FILA; j++) {
							ButacaModel butaca = new ButacaModel();
							nombreButaca = "" + letras[i] + "-" + j;
							butaca.setNombre(nombreButaca);
							butaca.setOcupado(false);
							butaca.setAlta(new Date());
							modelsButacas.add(butaca);
							butaca = null;
						}
					}
					System.out.println("-- Cargadas butacas hasta penultima fila");

					for (int i = 0; i < cantButacasUltimaFila; i++) {
						ButacaModel butaca = new ButacaModel();
						nombreButaca = "" + letras[(cantidadFilas)] + "-" + i;
						butaca.setNombre(nombreButaca);
						butaca.setOcupado(false);
						butaca.setAlta(new Date());
						modelsButacas.add(butaca);
						butaca = null;
					}
					System.out.println("-- Cargadas butacas ultima fila");

					entityButacas = butacaConverter.modelsToEntities(modelsButacas);
					System.out.println("-- Convertidos los modelos butacas a entidades");

				}

			}
			modelsButacas.clear();

			System.out.println("-- seteando la sala");
			entity.setSala(entitySala);

			System.out.println("-- seteando butacas");
			entity.setButacas(entityButacas);

			System.out.println("PELICULA");
			Pelicula entityPelicula = null;
			if (model.getIdPelicula() != null) {
				System.out.println("-- convirtiendo la pelicula del model a entidad");
				entityPelicula = peliculaRepository.getOne(model.getIdPelicula());
			}
			System.out.println("-- seteando pelicula");
			entity.setPelicula(entityPelicula);

			System.out.println("COPY PROPERTIES");
			BeanUtils.copyProperties(model, entity);
		} catch (Exception e) {
			System.err.print(e.getMessage());
			throw new WebException("Error al convertir el modelo " + model.toString() + " a entidad");
		}

		return entity;
	}

	public FuncionModel entityToModel(Funcion entity) throws WebException {

		FuncionModel model = new FuncionModel();

		try {
			List<ButacaModel> modelButacas = new ArrayList<>();
			List<String> idsButacas = new ArrayList<>();
			HorarioModel modelHorario = new HorarioModel();
			String idHorario = "";

			if (entity.getHorario() != null) {
				modelHorario = horarioConverter.entityToModel(entity.getHorario());
			}
			
			model.setHorario(modelHorario);
			model.setIdHorario(idHorario);
			
			if (entity.getButacas() != null) {
				modelButacas = butacaConverter.entitiesToModels(entity.getButacas());
			}

			for (ButacaModel butacaModel : modelButacas) {
				idsButacas.add(butacaModel.getId());
			}

			model.setButacas(modelButacas);
			model.setIdsButacas(idsButacas);

			if (entity.getSala() != null) {
				model.setIdSala(entity.getSala().getId());
				model.setSala(salaConverter.entityToModel(salaRepository.getOne(entity.getSala().getId())));
			}

			if (entity.getPelicula() != null) {
				model.setIdPelicula(entity.getPelicula().getId());
				model.setPelicula(
						peliculaConverter.entityToModel(peliculaRepository.getOne(entity.getPelicula().getId())));
			}

			int cantidadVacios = 0, cantidadOcupados = 0;
			boolean llena = false;

			for (Butaca butaca : entity.getButacas()) {
				if (butaca.isOcupado()) {
					cantidadOcupados += 1;
				} else {
					cantidadVacios += 1;
				}
			}

			if (cantidadVacios == 0) {
				llena = true;
			}

			model.setCantidadOcupados(cantidadOcupados);
			model.setCantidadVacios(cantidadVacios);
			model.setLlena(llena);

			BeanUtils.copyProperties(entity, model);
		} catch (Exception e) {
			throw new WebException("Error al convertir la entidad " + entity.toString() + " a modelo");
		}
		return model;
	}

	public List<Funcion> modelsToEntities(List<FuncionModel> models) throws WebException {
		List<Funcion> entities = new ArrayList<>();
		for (FuncionModel model : models) {
			entities.add(modelToEntity(model));
		}
		return entities;
	}

	public List<FuncionModel> entitiesToModels(List<Funcion> entities) throws WebException {
		List<FuncionModel> models = new ArrayList<>();
		for (Funcion entity : entities) {
			models.add(entityToModel(entity));
		}
		return models;
	}

}
