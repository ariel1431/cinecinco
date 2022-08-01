package com.QuintoTrainee.CineCinco.models;

import java.io.Serializable;
import java.util.Base64;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import lombok.Data;
@Data
public class FotoModel implements Serializable {
	private static final long serialVersionUID = 6522896498689132123L;
	
	   private String id;
	   private String nombre;
	   private String mime;
	   @Lob
	   @Basic(fetch = FetchType.LAZY)
	   private byte[] contenido;
	   
	    public String getImgData() {
	        return Base64.getMimeEncoder().encodeToString(contenido);
	    }
}
