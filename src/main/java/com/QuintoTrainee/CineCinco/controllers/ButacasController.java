package com.QuintoTrainee.CineCinco.controllers;

import java.util.ArrayList;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.ButacaModel;
import com.QuintoTrainee.CineCinco.models.FuncionModel;
import com.QuintoTrainee.CineCinco.services.ButacaService;
import com.QuintoTrainee.CineCinco.services.FuncionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/butaca")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ButacasController {

	@Autowired
	FuncionService funcionService;
	@Autowired
	ButacaService butacaService;

	@GetMapping("/seleccionar/{idFuncion}")
	public String seleccionar(ModelMap model, @PathVariable String idFuncion) {

		try {
			FuncionModel funcion = funcionService.getFuncionModelById(idFuncion);
			model.addAttribute("funcion", funcion);

			Map<Integer, ArrayList<ButacaModel>> filasMap = funcionService.obtenerMapButacasOrdenado(funcion);

			model.addAttribute("filasMap", filasMap);

			int cantFilas = funcionService.obtenerCantidadDeFilas(funcion) + 1;

			model.addAttribute("cantFilas", cantFilas);

			FuncionModel funcionSeleccionButacas = new FuncionModel();
			model.addAttribute("funcionSeleccionButacas", funcionSeleccionButacas);

		} catch (WebException e) {
			e.printStackTrace();
		}

		return "butaca_copy.html";
	}

	@PostMapping("/compra_entradas")
	public String test(ModelMap model,
			@Valid @ModelAttribute("funcionSeleccionButacas") FuncionModel funcionSeleccionButacas,
			@RequestParam(required = true) String idFuncion) {

		try {
			ArrayList<ButacaModel> butacas = new ArrayList<ButacaModel>();

			for (String idButaca : funcionSeleccionButacas.getIdsButacas()) {
				ButacaModel butaca = new ButacaModel();

				butaca = butacaService.getButacaModelById(idButaca);

				butacas.add(butaca);
			}

			for (ButacaModel butacaModel : butacas) {
				System.out.println(butacaModel.getNombre());
				System.out.println(butacaModel.getId());
			}

			FuncionModel funcion = funcionService.getFuncionModelById(idFuncion);
			int cantEntradas = butacas.size();
			double totalPagar = cantEntradas * funcion.getPrecioEntrada();

			model.addAttribute("butacas", butacas);
			model.addAttribute("funcion", funcion);
			model.addAttribute("totalPagar", totalPagar);
			model.addAttribute("cantEntradas", cantEntradas);

			return "pago_copy.html";

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/butaca/seleccionar/" + idFuncion;
	}

}
