package com.QuintoTrainee.CineCinco.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.QuintoTrainee.CineCinco.converters.BoletoConverter;
import com.QuintoTrainee.CineCinco.converters.CompraConverter;
import com.QuintoTrainee.CineCinco.entities.Boleto;
import com.QuintoTrainee.CineCinco.entities.Compra;
import com.QuintoTrainee.CineCinco.entities.Usuario;
import com.QuintoTrainee.CineCinco.models.BoletoModel;
import com.QuintoTrainee.CineCinco.models.ButacaModel;
import com.QuintoTrainee.CineCinco.models.CompraModel;
import com.QuintoTrainee.CineCinco.models.FuncionModel;
import com.QuintoTrainee.CineCinco.repositories.CompraRepository;
import com.QuintoTrainee.CineCinco.repositories.UsuarioRepository;
import com.QuintoTrainee.CineCinco.services.BoletoService;
import com.QuintoTrainee.CineCinco.services.ButacaService;
import com.QuintoTrainee.CineCinco.services.CompraService;
import com.QuintoTrainee.CineCinco.services.FuncionService;
import com.QuintoTrainee.CineCinco.services.NotificacionMail;
import com.QuintoTrainee.CineCinco.services.UsuarioService;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;

import lombok.RequiredArgsConstructor;
import lombok.var;
import net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.ForAbstractMethod;

@Controller
@RequestMapping("/compra")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompraController {
	@Autowired
	private CompraService compraService;
	@Autowired
	private CompraRepository compraRepository;
	@Autowired
	private CompraConverter compraConverter;
	@Autowired
	private FuncionService funcionService;
	@Autowired
	private ButacaService butacaService;
	@Autowired
	private BoletoService boletoService;
	@Autowired
	private BoletoConverter boletoConverter;
	@Autowired
	private HttpSession session;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private NotificacionMail notificacionMail;

	@PostMapping("/realizar_pago")
	public String crearPago(@RequestParam(required = true) String idFuncion,
			@RequestParam(required = true) String totalPagar, @RequestParam(required = true) List<String> idsButacas)
			throws MPException {

		CompraModel compraModel = new CompraModel();
		ArrayList<BoletoModel> boletos = new ArrayList<BoletoModel>();

		Usuario usuario = usuarioRepository.getOne(((Usuario) session.getAttribute("usuarioSession")).getId());

		try {
			
			List<CompraModel> comprasBasura = compraConverter.entitiesToModels(compraRepository.getComprasBasura());
			
			//eliminando las compras que se vieron interrumpidas
			if (!comprasBasura.isEmpty()) {
				CompraModel compraBasura;
				
				for(int i = 0; i < comprasBasura.size(); i++) {
					compraBasura = comprasBasura.get(i);
					comprasBasura.remove(compraBasura);
					compraService.eliminarCompraBasura(compraBasura);
					compraBasura = null;
					System.out.println("compra supuestamente eliminada");
				}
			}
			
			FuncionModel funcion = funcionService.getFuncionModelById(idFuncion);

			for (String idButaca : idsButacas) {
				ButacaModel butaca = butacaService.getButacaModelById(idButaca);

				BoletoModel boleto = new BoletoModel();

				boleto.setFuncion(funcion);
				boleto.setButaca(butaca);
				boleto = boletoConverter.entityToModel(boletoService.guardar(boleto));

				boletos.add(boleto);
			}

			float precioTotal = (float) (boletos.size() * funcion.getPrecioEntrada());

			compraModel.setBoletos(boletos);
			compraModel.setPrecioTotal(precioTotal);

			Compra compraEntity = compraService.guardar(compraModel);

			usuarioService.agregarCompra(usuario, compraEntity);

			// SECCION MP
			Preference preference = new Preference();
			// Crea un ítem en la preferencia
			Item item = new Item();
			item.setTitle("Entradas para: " + funcion.getPelicula().getTitulo()).setQuantity(boletos.size())
					.setUnitPrice((float) funcion.getPrecioEntrada());

			preference.appendItem(item);
			BackUrls backUrls = new BackUrls("http://localhost:8080/compra/save",
					"http://localhost:8080/compra/realizar_pago", "http://localhost:8080/compra/save");
			preference.setBackUrls(backUrls);
			var resulset = preference.save();
			return "redirect:" + resulset.getSandboxInitPoint();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/";
	}

	@GetMapping("/save")
	public String save(@RequestParam String status, @RequestParam String payment_type) throws Exception {

		Usuario usuario = usuarioRepository.getOne(((Usuario) session.getAttribute("usuarioSession")).getId());
		Compra compra = usuarioService.getCompraPendiente(usuario);

		System.out.println(usuario.getId());

		if (status.equals("approved")) {
			System.out.println(status);

			String nombresButacas = " ";
			for (Boleto boleto : compra.getBoletos()) {
				butacaService.ocuparButaca(boleto.getButaca());
				nombresButacas = nombresButacas + boleto.getButaca().getNombre() + ", ";
			}

			compra.setFechaAprobacionPago(new Date());

			compraRepository.save(compra);
			
			String cantBoletos = String.valueOf(compra.getBoletos().size());
			String salaNombre = compra.getBoletos().get(0).getFuncion().getSala().getNombre();
			String horaFuncion = compra.getBoletos().get(0).getFuncion().getHorario().getHora();
			String nombrePelicula = compra.getBoletos().get(0).getFuncion().getPelicula().getTitulo();
			
			notificacionMail.enviar("Ha comprado " + cantBoletos + " boletos para la pelicula: " + nombrePelicula + ", sus butacas seleccionadas son:" +nombresButacas+ "de la sala " +salaNombre+ " para la función de las " + horaFuncion + " hs. ","Se ha confirmado su compra.", usuario.getEmail());

		} else {
			System.out.println(status);
			usuarioService.eliminarCompra(usuario, compra);
		}

		return "redirect:/?status=" + status;

	}

}
