package com.QuintoTrainee.CineCinco.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuintoTrainee.CineCinco.converters.CompraConverter;
import com.QuintoTrainee.CineCinco.entities.Boleto;
import com.QuintoTrainee.CineCinco.entities.Butaca;
import com.QuintoTrainee.CineCinco.entities.Compra;
import com.QuintoTrainee.CineCinco.entities.Usuario;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.CompraModel;
import com.QuintoTrainee.CineCinco.repositories.ButacaRepository;
import com.QuintoTrainee.CineCinco.repositories.CompraRepository;
import com.QuintoTrainee.CineCinco.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompraService {

	@Autowired
	private CompraConverter compraConverter;
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private ButacaRepository butacaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public void validar(CompraModel compraModel) throws WebException {
		
		try {
			//Enum.valueOf(Estado.class, compraModel.getEstado().toString());
		} catch (IllegalArgumentException e) {
			throw new WebException("El estado indicado no es un estado valido");
		} catch (NullPointerException e) {
			throw new WebException("La compra debe tener un estado");
		}
	}
	
	public Compra guardar(CompraModel compraModel) throws WebException {
		
		validar(compraModel);
		
		Compra compraEntity = compraConverter.modelToEntity(compraModel);
		
		if (compraEntity.getAlta() != null) {
			compraEntity.setModificacion(new Date());
		} else {
			compraEntity.setAlta(new Date());
		}
		
		return compraRepository.save(compraEntity);
		
	}
	
	public void hardDelete(CompraModel compraModel) throws WebException {
		Compra compraEntity = compraConverter.modelToEntity(compraModel);
		compraRepository.delete(compraEntity);
	}
	
	public void hardDelete(Compra compra) throws WebException {
		compraRepository.delete(compra);
	}
	
	public Compra softDelete(CompraModel compraModel) throws WebException {
		Compra compraEntity = compraConverter.modelToEntity(compraModel);
		compraEntity.setBaja(new Date());
		return compraRepository.save(compraEntity);
	}

	public void eliminarCompraBasura(CompraModel compraBasura) throws WebException {
		
		Compra compra = compraConverter.modelToEntity(compraBasura);
		compraBasura = null;
		
		Usuario usuario = usuarioRepository.getUsuarioByCompra(compra.getId());
		
		System.out.println(usuario.getUsername());
		System.out.println(usuario.getCompras().remove(compra));
		usuarioRepository.save(usuario);
		
		System.out.println("final del metodo eliminar compra basura");
	}

}
