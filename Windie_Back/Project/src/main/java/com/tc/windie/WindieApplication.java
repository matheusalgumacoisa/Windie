package com.tc.windie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import apiVisao.ApiManterUsuario;

@SpringBootApplication
@ComponentScan(basePackageClasses = ApiManterUsuario.class)
public class WindieApplication {

	public static void main(String[] args) {
		SpringApplication.run(WindieApplication.class, args);
	}

}
