package com.QuintoTrainee.CineCinco.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.QuintoTrainee.CineCinco.enums.Provider;
import com.QuintoTrainee.CineCinco.enums.Rol;

import lombok.Data;

@Data
@Entity
public class Usuario {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String password;

	private String email;

	private String nombreCompleto;

	private String username;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaNacimiento;
	
	@Enumerated(EnumType.STRING)
	private Provider provider;

	private String infoTarjeta;

	
	
	@Enumerated(EnumType.STRING)
	private Rol rol;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Compra> compras;

	@Temporal(TemporalType.TIMESTAMP)
	private Date alta;

	@Temporal(TemporalType.TIMESTAMP)
	private Date baja;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modificacion;

}
