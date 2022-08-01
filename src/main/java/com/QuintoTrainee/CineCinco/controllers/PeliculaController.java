package com.QuintoTrainee.CineCinco.controllers;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.QuintoTrainee.CineCinco.enums.Genero;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.PeliculaModel;
import com.QuintoTrainee.CineCinco.services.PeliculaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/pelicula")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PeliculaController {
	
	@Autowired
	private PeliculaService peliculaService;


	@GetMapping
	public String peliculas(ModelMap model) {
		List<PeliculaModel> estrenos;
		
		try {
			estrenos = peliculaService.listarEstrenos();
			model.put("estrenos", estrenos);
			
			List<PeliculaModel> enCartel = peliculaService.listarEnCartel();
			model.put("enCartel", enCartel);
			
		} catch (WebException e) {
			e.printStackTrace();
		}
		
		return "peliculas.html";
	}
	
	@GetMapping("/por_genero")
	public String peliculaporgenero(ModelMap modelo, @RequestParam(required = false) String genero) {

		try {
			List<PeliculaModel> peliculaxgenero;

			List<PeliculaModel> cartelxgenero;
			

			Set<Genero> generos = EnumSet.allOf(Genero.class);
			generos.remove(Genero.DESHABILITADOS);

			if (genero != null) {
				peliculaxgenero = peliculaService.listarPeliculasPorGenero(Genero.valueOf(genero));
				
				
			} else {
				peliculaxgenero = peliculaService.listarPeliculasActivasModels();
				
			}
			modelo.put("peliculas", peliculaxgenero);
			
			if (genero != null) {
			
			cartelxgenero = peliculaService.listarPeliculasPorGeneroEnCartel(Genero.valueOf(genero));
			
		} else {
			
			cartelxgenero = peliculaService.listarPeliculasActivasModels();
		}
		
		modelo.put("generoEnCartel", cartelxgenero);

	} 
		
		catch (Exception ex) {
			modelo.put("error", ex.getMessage());

		}
		return "peliculasporgenero.html";
	}

}	
