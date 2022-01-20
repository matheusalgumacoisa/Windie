package com.tc.windie;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.xml.sax.SAXException;

import com.tc.windie.apiPagSeguro.ApiCheckout;

@SpringBootApplication
@ComponentScan//(basePackageClasses = ApiManterUsuario.class)
@Configuration
@EnableScheduling
public class WindieApplication {

	public static void main(String[] args){
		SpringApplication.run(WindieApplication.class, args);
	}

}
