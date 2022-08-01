package com.QuintoTrainee.CineCinco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;

@SpringBootApplication
@EnableAsync
public class CineCincoApplication  {

	public static void main(String[] args) throws MPConfException {
		SpringApplication.run(CineCincoApplication.class, args);
		MercadoPago.SDK.setAccessToken("APP_USR-4715757626164033-080123-a8743bc5b5921df98b051e7860a9aa6e-800504782");
		
	}


}

