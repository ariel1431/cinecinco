package com.QuintoTrainee.CineCinco.converters;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.QuintoTrainee.CineCinco.exceptions.WebException;

public abstract class Converter<M extends Object, E extends Object> {

	public abstract E modelToEntity(M m) throws WebException;

	public abstract M entityToModel(E e) throws WebException;
	
	public abstract List<E> modelsToEntities(List<M> m) throws WebException;
	
	public abstract List<M> entitiesToModels(List<E> e) throws WebException;
	
	protected Log log;

	public Converter() {
		this.log = LogFactory.getLog(getClass());
	}
	
}
