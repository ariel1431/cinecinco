package com.QuintoTrainee.CineCinco.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.QuintoTrainee.CineCinco.enums.Estado;

import lombok.Data;

@Data
public class CompraModel implements Serializable {

	private static final long serialVersionUID = 5257352994921354837L;

	private String id;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fechaCompra;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fechaAprobacionPago;

	private List<BoletoModel> boletos;
	private List<String> idsBoletos;
	private String boletosSeleccionados;

	private double precioTotal;

	private Estado estado;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date alta;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date baja;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modificacion;

}
