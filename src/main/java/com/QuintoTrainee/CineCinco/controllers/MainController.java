package com.QuintoTrainee.CineCinco.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.QuintoTrainee.CineCinco.converters.UsuarioConverter;
import com.QuintoTrainee.CineCinco.entities.Pelicula;
import com.QuintoTrainee.CineCinco.entities.Usuario;
import com.QuintoTrainee.CineCinco.enums.Rol;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.PeliculaModel;
import com.QuintoTrainee.CineCinco.models.UsuarioModel;
import com.QuintoTrainee.CineCinco.repositories.UsuarioRepository;
import com.QuintoTrainee.CineCinco.services.NotificacionMail;
import com.QuintoTrainee.CineCinco.services.PeliculaService;
import com.QuintoTrainee.CineCinco.services.UsuarioService;
import com.QuintoTrainee.CineCinco.utils.UtilDate;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MainController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private HttpSession session;
	@Autowired
	private UsuarioConverter usuarioConverter;
	@Autowired
	private PeliculaService peliculaService;
	@Autowired
    private NotificacionMail notificacionMail;

	@GetMapping("/")
	public String index(ModelMap model) {

		try {
			List<PeliculaModel> peliculas = peliculaService.listarPeliculasActivasModels();
			model.put("peliculas", peliculas);
			
			for (PeliculaModel peliculaModel : peliculas) {
				System.out.println(peliculaModel.getTitulo());
				System.out.println(peliculaModel.getFoto().getMime());
			}

			List<PeliculaModel> estrenos = peliculaService.listarEstrenos();
			model.put("estrenos", estrenos);
			
			List<PeliculaModel> enCartel = peliculaService.listarEnCartel();
			model.put("enCartel", enCartel);
			
			List<PeliculaModel> top3 = peliculaService.listarTop3();
			model.put("top3", top3);
			
		} catch (WebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "index";
	}



	@GetMapping("/inicio")
	public String inicio(ModelMap model) {
		
		List<PeliculaModel> peliculas;
		try {
			peliculas = peliculaService.listarPeliculasActivasModels();
			model.put("peliculas", peliculas);
			
			List<PeliculaModel> estrenos = peliculaService.listarEstrenos();
			model.put("estrenos", estrenos);
			
			List<PeliculaModel> enCartel = peliculaService.listarEnCartel();
			model.put("enCartel", enCartel);
			
			List<PeliculaModel> top3 = peliculaService.listarTop3();
			model.put("top3", top3);
			
		} catch (WebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "index.html";
	}



	// REGISTRO
	@GetMapping("/registro")
	public String registro(ModelMap modelo) {
		modelo.addAttribute("usuario", new UsuarioModel());
		return "registro.html";
	}

	@PostMapping("/registrar_usuario")
	public String registrarUsuario(ModelMap modelo, @Valid @ModelAttribute("usuario") UsuarioModel usuarioModel,
			@RequestParam(required = true) String password, @RequestParam(required = true) String repeated_password,
			@RequestParam(required = true) String fecha_nacimiento) throws Exception {

		try {
			usuarioModel.setRol(Rol.CLIENTE);
			usuarioModel.setFechaNacimiento(UtilDate.parseFechaGuiones(fecha_nacimiento));
			usuarioService.guardar(usuarioModel, password, repeated_password);
			
		} catch (Exception ex) {
			modelo.addAttribute("usuario", usuarioModel);
			modelo.addAttribute("error", ex.getMessage());
			return "redirect:/";
		}

		notificacionMail.enviar("Se ha registrado correctamente.", "CineCinco registro correcto", usuarioModel.getEmail());
		return "redirect:/login";
	}

	// LOGIN
	@GetMapping("/login")
	public String login(ModelMap modelo, @RequestParam(required = false) String error) throws WebException {

		if (error != null && !error.isEmpty()) {
			modelo.addAttribute("error", "La dirección de mail o la contraseña que ingresó son incorrectas.");
		}
		return "login";

	}

}
