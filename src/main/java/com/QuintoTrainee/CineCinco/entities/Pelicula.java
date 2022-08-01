package com.QuintoTrainee.CineCinco.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.QuintoTrainee.CineCinco.enums.Genero;

import lombok.Data;

@Data
@Entity
public class Pelicula {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String titulo;
	
	private String trailer;

	@Lob
	@Column(name = "sinopsis", length = 4000)
	private String sinopsis;

	@Enumerated(EnumType.STRING)
	private Genero genero;

	@Temporal(TemporalType.TIMESTAMP)
	private Date alta;

	@Temporal(TemporalType.TIMESTAMP)
	private Date baja;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modificacion;
    
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Foto foto;
}
