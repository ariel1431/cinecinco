package com.QuintoTrainee.CineCinco.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuintoTrainee.CineCinco.converters.SalaConverter;
import com.QuintoTrainee.CineCinco.entities.Compra;
import com.QuintoTrainee.CineCinco.entities.Sala;
import com.QuintoTrainee.CineCinco.enums.Estado;
import com.QuintoTrainee.CineCinco.exceptions.WebException;
import com.QuintoTrainee.CineCinco.models.CompraModel;
import com.QuintoTrainee.CineCinco.models.SalaModel;
import com.QuintoTrainee.CineCinco.repositories.SalaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SalaService {
	@Autowired
	private SalaConverter salaConverter;
	
	@Autowired
	private SalaRepository salaRepository;
	
	public void validar(SalaModel salaModel) throws WebException {
	
	if(salaModel.getNombre()==null|| salaModel.getNombre().isEmpty() || salaModel.getNombre().equals("")) {
		throw new WebException("La Sala tiene que tener un nombre");
	}
	
	if(salaModel.getCantidadButacas()==0 ) {
		throw new WebException("La Sala tiene que tener almenos una butaca");
	}
	}
	
	public Sala guardar(SalaModel salaModel) throws WebException{
		validar(salaModel);
		
		Sala salaEntity = salaConverter.modelToEntity(salaModel);
		
		if (salaEntity.getAlta() != null) {
			salaEntity.setModificacion(new Date());
		} else {
		   salaEntity.setAlta(new Date());
		}
		
		return salaRepository.save(salaEntity);
	}
	
	public void hardDelete(SalaModel salaModel) throws WebException {
		Sala salaEntity = salaConverter.modelToEntity(salaModel);
		salaRepository.delete(salaEntity);
	}
	
	public Sala softDelete(String idSala) throws WebException {
		Sala salaEntity = salaRepository.getOne(idSala);
		salaEntity.setBaja(new Date());
		return salaRepository.save(salaEntity);
	}

	public List<SalaModel> listarSalasActivasModels() throws WebException {
		return salaConverter.entitiesToModels(salaRepository.listarSalasActivas());
	}
}
