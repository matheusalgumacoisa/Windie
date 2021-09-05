package com.tc.windie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import apiVisao.VisaoUsuario;

@SpringBootApplication
@ComponentScan(basePackageClasses = VisaoUsuario.class)
public class WindieApplication {

	public static void main(String[] args) {
		SpringApplication.run(WindieApplication.class, args);
	}

}
