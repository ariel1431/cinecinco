package com.QuintoTrainee.CineCinco.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.QuintoTrainee.CineCinco.enums.Rol;

import lombok.Data;

@Data
public class UsuarioModel implements Serializable {

	private static final long serialVersionUID = -2785730486451467206L;

	private String id;

	private String nombreCompleto;

	private String username;

	private String email;

	private String infoTarjeta;

	private Rol rol;

	private List<CompraModel> compras;
	private List<String> idsCompras;
	private String comprasSeleccionadas;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fechaNacimiento;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date alta;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date baja;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modificacion;
}