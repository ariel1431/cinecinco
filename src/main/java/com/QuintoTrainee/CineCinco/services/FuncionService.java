package com.QuintoTrainee.CineCinco.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuintoTrainee.CineCinco.converters.FuncionConverter;
import com.QuintoTrainee.CineCinco.entities.Funcion;
import com.QuintoTrainee.CineCinco.entities.Pelicula;
import com.QuintoTrainee.CineCinco.enums.Idioma;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.ButacaModel;
import com.QuintoTrainee.CineCinco.models.FuncionModel;
import com.QuintoTrainee.CineCinco.repositories.FuncionRepository;
import com.QuintoTrainee.CineCinco.repositories.PeliculaRepository;

import static com.QuintoTrainee.CineCinco.utils.Texts.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FuncionService {

	@Autowired
	private FuncionRepository funcionRepository;
	@Autowired
	private FuncionConverter funcionConverter;

	@Autowired
	private PeliculaRepository peliculaRepository;
	
	public void validar(FuncionModel funcionModel) throws WebException {

		if (funcionModel.getFecha() == null) {
			throw new WebException("La funcion debe contar con una fecha");
		}

		if (funcionModel.getHorario() == null) {
			throw new WebException("La funcion debe contar con un horario");
		}

		if (funcionModel.getPrecioEntrada() < 0) {
			throw new WebException("El precio de entrada indicado para la funcion es invalido");
		}

		try {
			Enum.valueOf(Idioma.class, funcionModel.getIdioma().toString());
		} catch (IllegalArgumentException e) {
			throw new WebException("El idioma indicado no es un idioma valido");
		} catch (NullPointerException e) {
			throw new WebException("La funcion debe contener un idioma");
		}

	}

	// persiste el modelo
	public Funcion guardar(FuncionModel funcionModel) {

		try {

			validar(funcionModel);
			System.out.println("antes de convertir la funcion");
			Funcion funcionEntity = funcionConverter.modelToEntity(funcionModel);
			System.out.println("despues de convertir la funcion");

			if (funcionEntity.getAlta() != null) {
				funcionEntity.setModificacion(new Date());
			} else {
				funcionEntity.setAlta(new Date());
			}

			return funcionRepository.save(funcionEntity);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public void hardDelete(FuncionModel funcionModel) throws WebException {
		Funcion funcionEntity = funcionConverter.modelToEntity(funcionModel);
		funcionRepository.delete(funcionEntity);
	}

	public Funcion softDelete(String idFuncion) throws WebException {
		Funcion funcionEntity = funcionRepository.getOne(idFuncion);
		funcionEntity.setBaja(new Date());
		return funcionRepository.save(funcionEntity);
	}

	public List<FuncionModel> listarFuncionesActivasModels() throws WebException {
		return funcionConverter.entitiesToModels(funcionRepository.listarFuncionesActivas());
	}

	public FuncionModel getFuncionModelById(String idFuncion) throws WebException {
		return funcionConverter.entityToModel(funcionRepository.getOne(idFuncion));
	}

	public int obtenerCantidadDeFilas(FuncionModel funcion) {

		int cantButacas = funcion.getSala().getCantidadButacas();
		int cantidadFilas = (int) Math.floor(cantButacas / (double) BUTACAS_POR_FILA);

		return cantidadFilas;
	}

	public int obtenerCantidadButacasUltimaFila(FuncionModel funcion) {

		int cantButacas = funcion.getSala().getCantidadButacas();
		int cantidadFilas = (int) Math.floor(cantButacas / (double) BUTACAS_POR_FILA);
		int cantButacasUltimaFila = cantButacas - (BUTACAS_POR_FILA * cantidadFilas);

		return cantButacasUltimaFila;
	}

	public Map<Integer, ArrayList<ButacaModel>> obtenerMapButacasOrdenado(FuncionModel funcion) {

		int cantidadFilas = this.obtenerCantidadDeFilas(funcion);
		int cantidadButacasUltimaFila = this.obtenerCantidadButacasUltimaFila(funcion);

		List<ButacaModel> butacasFuncion = funcion.getButacas();
		
		Collections.sort(butacasFuncion, comparatorByNombreButaca);
		
		Map<Integer, ArrayList<ButacaModel>> filasMap = new HashMap<>();

		for (int i = 0; i < cantidadFilas; i++) {
			ArrayList<ButacaModel> arrayButacas = new ArrayList<ButacaModel>();

			for (int j = 0; j < (BUTACAS_POR_FILA); j++) {
				arrayButacas.add(butacasFuncion.get(j + (10 * i)));
			}

			filasMap.put(i, arrayButacas);
		}

		ArrayList<ButacaModel> arrayButacas = new ArrayList<ButacaModel>();
		for (int i = 0; i < cantidadButacasUltimaFila; i++) {

			arrayButacas.add(butacasFuncion.get(i + (cantidadFilas * 10)));

		}
		filasMap.put(cantidadFilas, arrayButacas);

		return filasMap;
	}

	// CUSTOM COMPARATORS
	Comparator<ButacaModel> comparatorByNombreButaca = new Comparator<ButacaModel>() {
		@Override
		public int compare(ButacaModel b1, ButacaModel b2) {
			return b1.getNombre().compareTo(b2.getNombre());
		}
	};

	public List<FuncionModel> listarFuncionesActivasPorPelicula(String idPelicula) throws WebException {
		Pelicula pelicula = peliculaRepository.getOne(idPelicula);
		return funcionConverter.entitiesToModels(funcionRepository.listarFuncionesActivasPorPelicula(pelicula));
	}
	

}
