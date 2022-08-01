package com.QuintoTrainee.CineCinco.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BoletoModel implements Serializable {

	private static final long serialVersionUID = 1586023621114676267L;

	private String id;

	private FuncionModel funcion;
	private String idFuncion;

	private ButacaModel butaca;
	private String idButaca;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date alta;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date baja;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modificacion;

}
