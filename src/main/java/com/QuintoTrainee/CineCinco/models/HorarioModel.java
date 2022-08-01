package com.QuintoTrainee.CineCinco.models;

import java.io.Serializable;

import lombok.Data;
@Data
public class HorarioModel implements Serializable {

	private static final long serialVersionUID = 4758698951798528563L;
	private String id;
	private String hora;
}
