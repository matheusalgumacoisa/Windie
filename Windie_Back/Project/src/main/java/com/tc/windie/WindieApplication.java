package com.tc.windie;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan//(basePackageClasses = ApiManterUsuario.class)
@Configuration
@EnableScheduling
public class WindieApplication {

	public static void main(String[] args) throws SQLException{
		SpringApplication.run(WindieApplication.class, args);

	}

}
