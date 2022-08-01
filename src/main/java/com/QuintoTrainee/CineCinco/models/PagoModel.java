package com.QuintoTrainee.CineCinco.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
@Data
public class PagoModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -298992534404672712L;
	
	private String id;
    private Double total;
    private String status;
    private String tipoPago;
    private String idUsuario;
    private String idTurno;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fecha;

}
