package com.QuintoTrainee.CineCinco.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SalaModel implements Serializable {

	private static final long serialVersionUID = -1142482462549345236L;

	private String id;

	private String nombre;

	private int cantidadButacas;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date alta;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date baja;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modificacion;

}
