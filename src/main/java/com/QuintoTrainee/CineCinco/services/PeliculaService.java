package com.QuintoTrainee.CineCinco.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.QuintoTrainee.CineCinco.models.PeliculaModel;
import com.QuintoTrainee.CineCinco.repositories.PeliculaOrdenadaPorGenero;
import com.QuintoTrainee.CineCinco.repositories.PeliculaRepository;
import com.QuintoTrainee.CineCinco.converters.PeliculaConverter;
import com.QuintoTrainee.CineCinco.entities.Foto;
import com.QuintoTrainee.CineCinco.entities.Pelicula;
import com.QuintoTrainee.CineCinco.enums.Genero;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
//import org.apache.commons.lang3.EnumUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PeliculaService {

	@Autowired
	private PeliculaRepository peliculaRepository;

	@Autowired
	private PeliculaOrdenadaPorGenero peliculaOrdenadaPorGenero;
	
	@Autowired
	private PeliculaConverter peliculaConverter;
	
	@Autowired
	private FotoService fotoService;
	
	public void hardDelete(PeliculaModel model) {
		Pelicula pelicula = peliculaRepository.getOne(model.getId());
		String idFoto = pelicula.getFoto().getId();
		pelicula.setFoto(null);
		peliculaRepository.delete(pelicula);
		fotoService.hardDelete(idFoto);
	}

	public void hardDelete(String idPelicula) {
		Pelicula pelicula = peliculaRepository.getOne(idPelicula);
		String idFoto = pelicula.getFoto().getId();
		pelicula.setFoto(null);
		peliculaRepository.delete(pelicula);
		fotoService.hardDelete(idFoto);
	}
	
	public Pelicula softDelete(String idPelicula) {
		Pelicula pelicula = peliculaRepository.getOne(idPelicula);
		String idFoto = pelicula.getFoto().getId();
		pelicula.setFoto(null);
		pelicula.setBaja(new Date());		
		fotoService.hardDelete(idFoto);
		return peliculaRepository.save(pelicula);
	}

	public Pelicula guardar(PeliculaModel model, MultipartFile fotoPelicula) throws Exception {

		validar(model);

		Pelicula entity = peliculaConverter.modelToEntity(model);
		if (fotoPelicula == null || fotoPelicula.isEmpty()) {
			throw new WebException("La película debe tener una foto");
		}
		entity.setFoto(fotoService.guardar(fotoPelicula));
		
		if (entity.getAlta() != null) {
			entity.setModificacion(new Date());
		} else {
			entity.setAlta(new Date());
		}

		return peliculaRepository.save(entity);
	}
	
	public Pelicula guardar(PeliculaModel model, Foto fotoPelicula) throws Exception {

		validar(model);

		Pelicula entity = peliculaConverter.modelToEntity(model);
		if (fotoPelicula == null) {
			throw new WebException("La película debe tener una foto");
		}
		entity.setFoto(fotoPelicula);
		
		if (entity.getAlta() != null) {
			entity.setModificacion(new Date());
		} else {
			entity.setAlta(new Date());
		}

		return peliculaRepository.save(entity);
	}

	public void validar(PeliculaModel peliculaM) throws WebException {

		if (peliculaM.getTitulo() == null || peliculaM.getTitulo().isEmpty() || peliculaM.getTitulo().equals("")) {
			throw new WebException("La pelicula debe tener un titulo");
		}
		
		//Añadí el trailer para poder llamarlo en la portada//
		if (peliculaM.getTrailer() == null || peliculaM.getTrailer().isEmpty() || peliculaM.getTrailer().equals("")) {
			throw new WebException("La pelicula debe tener un trailer");
		}

		if (peliculaM.getSinopsis() == null || peliculaM.getSinopsis().isEmpty()
				|| peliculaM.getSinopsis().equals("")) {
			throw new WebException("La pelicula debe tener una sinopsis");
		}

		if (peliculaM.getSinopsis().length() > 4000) {
			throw new WebException("La sinopsis de la pelicula debe ser menor de 4000 caracteres");
		}

		if (peliculaM.getGenero() == null) {
			throw new WebException("La pelicula debe tener un genero");
		}
		
		Pelicula peliculaE = peliculaRepository.buscarPorTitulo(peliculaM.getTitulo());

		if (peliculaE != null && !peliculaE.getId().equals(peliculaM.getId())) {
			throw new WebException("Ya existe una pelicula con ese titulo");
		}

		try {
			Enum.valueOf(Genero.class, peliculaM.getGenero().toString());
		} catch (IllegalArgumentException e) {
			throw new WebException("El genero indicado no es un genero valido");
		} catch (NullPointerException e) {
			throw new WebException("La pelicula debe contener un genero");
		}

	}

	public List<PeliculaModel> listarPeliculasActivasModels() throws WebException {
		return peliculaConverter.entitiesToModels(peliculaRepository.listarPeliculasActivas());
	}
	
	public List<PeliculaModel> listarEstrenos() throws WebException{
		return peliculaConverter.entitiesToModels(peliculaRepository.listarEstrenos());
	}
	
	public List<PeliculaModel> listarEnCartel() throws WebException{
		return peliculaConverter.entitiesToModels(peliculaRepository.listarEnCartel());
	}

	public List<PeliculaModel> listarPeliculasPorGenero(Genero genero) throws WebException{
		return peliculaConverter.entitiesToModels(peliculaOrdenadaPorGenero.buscarPorGenero(4, genero));
	}

	public List<PeliculaModel> listarPeliculasPorGeneroEnCartel(Genero genero) throws WebException{
		return peliculaConverter.entitiesToModels(peliculaOrdenadaPorGenero.buscarPorGeneroEnCartel(4, genero));
	}
	
	public PeliculaModel getPeliculaModelById(String idPelicula) throws WebException {
		return peliculaConverter.entityToModel(peliculaRepository.getOne(idPelicula));
	}

	public List<PeliculaModel> listarTop3() throws WebException {
		return peliculaConverter.entitiesToModels(peliculaRepository.listarTop3());
	}


}
