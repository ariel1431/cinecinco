package com.QuintoTrainee.CineCinco.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.QuintoTrainee.CineCinco.Oauth2.CustomOAuth2User;
import com.QuintoTrainee.CineCinco.converters.UsuarioConverter;
import com.QuintoTrainee.CineCinco.entities.Boleto;
import com.QuintoTrainee.CineCinco.entities.Butaca;
import com.QuintoTrainee.CineCinco.entities.Compra;
import com.QuintoTrainee.CineCinco.entities.Usuario;
import com.QuintoTrainee.CineCinco.enums.Provider;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.UsuarioModel;
import com.QuintoTrainee.CineCinco.repositories.ButacaRepository;
import com.QuintoTrainee.CineCinco.repositories.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private UsuarioConverter usuarioConverter;
	@Autowired
	private CompraService compraService;
	@Autowired
	private ButacaRepository butacaRepository;

	public void validar(UsuarioModel usuarioModel, String password, String repeatedPass) throws WebException {

		if (usuarioModel.getAlta() != null) {
			if (usuarioModel.getId() == null || usuarioModel.getId().isEmpty() || usuarioModel.getId().equals("")) {
				throw new WebException("El Id esta vacio");
			}
		}
		if (usuarioModel.getEmail() == null || usuarioModel.getEmail().isEmpty()
				|| usuarioModel.getEmail().equals("")) {
			throw new WebException("El usuario tiene que tener un Email");
		}
		if (usuarioModel.getNombreCompleto() == null || usuarioModel.getNombreCompleto().isEmpty()
				|| usuarioModel.getNombreCompleto().equals("")) {
			throw new WebException("El usuario tiene que tener un nombre completo");
		}
		if (usuarioModel.getUsername() == null || usuarioModel.getUsername().isEmpty()
				|| usuarioModel.getUsername().equals("")) {
			throw new WebException("El usuario tiene que tener un Nombre de Usuario");
		}
		if (usuarioModel.getFechaNacimiento() == null) {
			throw new WebException("El Usuario tiene que tener una fecha de nacimiento");
		}

		DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();
		int comparacionFecha = dateOnlyComparator.compare(usuarioModel.getFechaNacimiento(), new Date());

		if (comparacionFecha >= 0) {
			throw new WebException("El Usuario tiene que tener una fecha de nacimiento anterior a la fecha actual");
		}

		if (usuarioModel.getRol() == null) {
			throw new WebException("El Usuario tiene que tener una Rol");
		}
		if (password == null || password.isEmpty() || password.length() <= 8) {
			throw new WebException("Contraseña invalida, debe tener como minimo 8 caracteres");
		}
		if (!password.equals(repeatedPass)) {
			throw new WebException("Las contraseñas deben ser iguales");
		}
	}

	public Usuario guardar(UsuarioModel usuarioModel, String password, String repeatedPass) throws WebException {

		validar(usuarioModel, password, repeatedPass);

		Usuario usuarioEntity = usuarioConverter.modelToEntity(usuarioModel);

		String encriptada = new BCryptPasswordEncoder().encode(password);
		usuarioEntity.setPassword(encriptada);

		if (usuarioEntity.getAlta() != null) {
			usuarioEntity.setModificacion(new Date());
		} else {
			usuarioEntity.setAlta(new Date());
		}

		return usuarioRepository.save(usuarioEntity);
	}

	public void hardDelete(UsuarioModel usuarioModel) throws WebException {
		Usuario usuarioEntity = usuarioConverter.modelToEntity(usuarioModel);
		usuarioRepository.delete(usuarioEntity);
	}

	public Usuario softDelete(UsuarioModel usuarioModel) throws WebException {
		Usuario usuarioEntity = usuarioConverter.modelToEntity(usuarioModel);
		usuarioEntity.setBaja(new Date());
		return usuarioRepository.save(usuarioEntity);
	}

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

		Usuario usuario = usuarioRepository.buscarPorMail(mail);

		if (usuario != null) {

			List<GrantedAuthority> permisos = new ArrayList<>();

			GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
			permisos.add(p1);

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usuarioSession", usuario);

			User user = new User(usuario.getUsername(), usuario.getPassword(), permisos);

			return user;

		} else {
			return null;
		}
	}

	public void processOAuthPostLogin(CustomOAuth2User oAuth2User) {
		Usuario existUser = usuarioRepository.buscarPorMail(oAuth2User.getEmail());

		if (existUser == null) {
			Usuario newUser = new Usuario();
			newUser.setNombreCompleto(oAuth2User.getName());
			newUser.setEmail(oAuth2User.getEmail());
			newUser.setProvider(Provider.GOOGLE);

			usuarioRepository.save(newUser);
		}

	}

	public void agregarCompra(Usuario usuario, Compra compraEntity) {

		usuario.getCompras().add(compraEntity);

		usuarioRepository.save(usuario);

	}

	public Compra getCompraPendiente(Usuario usuario) throws WebException {
		List<Compra> compraPendiente = usuarioRepository.getCompraPendiente(usuario.getId());
		Compra compra;
		if (compraPendiente.size() > 1) {
			for(int i = 1; i < compraPendiente.size(); i++) {
				this.eliminarCompra(usuario, compraPendiente.get(i));
			}
		}
		compra = compraPendiente.get(0);
		return compra;
	}

	public void eliminarCompra(Usuario usuario, Compra compra) throws WebException {

		usuario.getCompras().remove(compra);

		for (Boleto boleto : compra.getBoletos()) {
			Butaca butaca = boleto.getButaca();
			butaca.setOcupado(false);
			butacaRepository.save(butaca);
			butaca = null;
		}

		usuarioRepository.save(usuario);
		compraService.hardDelete(compra);
	}

}
