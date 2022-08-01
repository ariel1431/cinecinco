package com.QuintoTrainee.CineCinco.services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.QuintoTrainee.CineCinco.entities.Foto;
import com.QuintoTrainee.CineCinco.entities.Pelicula;
import com.QuintoTrainee.CineCinco.models.FotoModel;
import com.QuintoTrainee.CineCinco.models.PeliculaModel;
import com.QuintoTrainee.CineCinco.repositories.FotoRepository;

@Service
public class FotoService {

	@Autowired
	private FotoRepository fotoRepository;

	@Transactional
	public Foto guardar(MultipartFile archivo) throws Exception {

		if (archivo != null) {
			try {

				Foto foto = new Foto();

				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());

				return fotoRepository.save(foto);

			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

		return null;
	}
	
	public void hardDelete(FotoModel model) {
		Foto foto = fotoRepository.getOne(model.getId());
		fotoRepository.delete(foto);
	}

	public void hardDelete(String idFoto) {
		Foto foto = fotoRepository.getOne(idFoto);
		fotoRepository.delete(foto);
	}
}
