package com.QuintoTrainee.CineCinco.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.QuintoTrainee.CineCinco.entities.Foto;
import com.QuintoTrainee.CineCinco.enums.Genero;

import lombok.Data;

@Data
public class PeliculaModel implements Serializable {

	private static final long serialVersionUID = 6978266984025964947L;

	private String id;

	private String titulo;

	private String sinopsis;
	
	//trailer//
	private String trailer;

	private Genero genero;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date alta;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date baja;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modificacion;
	
	private FotoModel foto;
	private String idFoto;
}
