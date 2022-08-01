package com.QuintoTrainee.CineCinco.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuintoTrainee.CineCinco.converters.BoletoConverter;
import com.QuintoTrainee.CineCinco.entities.Boleto;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.BoletoModel;
import com.QuintoTrainee.CineCinco.repositories.BoletoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BoletoService {

@Autowired
private BoletoRepository boletoRepository;
	
@Autowired
private BoletoConverter boletoConverter;
	
	public void hardDelete(BoletoModel model) {
		Boleto boleto = boletoRepository.getOne(model.getId());
		boletoRepository.delete(boleto);
	}
	
	public Boleto softDelete(BoletoModel model) {
		Boleto boleto = boletoRepository.getOne(model.getId());
		boleto.setBaja(new Date());
		return boletoRepository.save(boleto);
	}
	
	public Boleto guardar(BoletoModel model) throws WebException {
		
		validar(model);
		
		Boleto entity = boletoConverter.modelToEntity(model);
		
		if(entity.getAlta() != null) {
			entity.setModificacion(new Date());
		}else {
			entity.setAlta(new Date());
		}
		
		return boletoRepository.save(entity);
	}
	
	public void validar(BoletoModel boletoB) throws WebException {
		
		if (boletoB.getFuncion() == null || boletoB.getFuncion().isLlena() || boletoB.getFuncion().equals("")) {
			throw new WebException("El boleto debe tener una función");
		}
		
		if (boletoB.getButaca() == null || boletoB.getButaca().equals("")) {
			throw new WebException("El boleto debe tener una butaca");
		}
		
		try {
			
		} catch (IllegalArgumentException e) {
			throw new WebException("El boleto indicado no es un boleto valido");
		} catch (NullPointerException e) {
			throw new WebException("El boleto debe tener un número");
		}
		
	}     
	
}

