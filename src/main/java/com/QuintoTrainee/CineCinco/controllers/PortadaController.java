package com.QuintoTrainee.CineCinco.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.FuncionModel;
import com.QuintoTrainee.CineCinco.models.PeliculaModel;
import com.QuintoTrainee.CineCinco.services.FuncionService;
import com.QuintoTrainee.CineCinco.services.PeliculaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/portada")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PortadaController {
	@Autowired
	private PeliculaService peliculaService;


	@Autowired
	private FuncionService funcionService;
	
    @GetMapping("/{idPelicula}")
	public String portada(ModelMap modelo, @PathVariable String idPelicula) {

		try {
			PeliculaModel pelicula = peliculaService.getPeliculaModelById(idPelicula);
			modelo.addAttribute("pelicula", pelicula);

			List<FuncionModel> funciones = funcionService.listarFuncionesActivasPorPelicula(idPelicula);
			
			List<FuncionModel> funcionesSubtituladas = new ArrayList<>();
			List<FuncionModel> funcionesCastellano = new ArrayList<>();
			
			for (FuncionModel funcionModel : funciones) {
				if (funcionModel.isSubtitulada()) {
					funcionesSubtituladas.add(funcionModel);
				} else {
					funcionesCastellano.add(funcionModel); //ACA ESTAMOS ASUMIENDO QUE SI NO ESTA SUBTITULADA ESTA EN CASTELLANO, HAY QUE CAMBIAR EL SISTEMA PARA TENER EN CUENTA LOS DEMAS IDIOMAS XDDDD
				}
			}
			
			modelo.addAttribute("funcionesSubtituladas", funcionesSubtituladas);
			modelo.addAttribute("funcionesCastellano", funcionesCastellano);
		} catch (WebException e) {

			e.printStackTrace();
		}

		return "portada.html";
	}

	@PostMapping("/redirigir/butacasFuncion")
	public String redirigirAButacasFuncion(ModelMap modelo, @RequestParam(required = true) String idFuncion) {
		return "redirect:/butaca/seleccionar/"+idFuncion;
	}
}
