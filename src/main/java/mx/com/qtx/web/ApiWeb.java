package mx.com.qtx.web;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import mx.com.qtx.entidades.Saludo;

@RestController
public class ApiWeb {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Environment env;
	
	private String urlCte = null;
	
	@PostConstruct
	public void inicializarUrlCte() {
		this.urlCte = "http://" + env.getProperty("mx.com.qtx.cliente01") 
		              + ":" + env.getProperty("mx.com.qtx.cliente01.port");
	}
	
	@GetMapping(path = "/testServicio", produces = MediaType.APPLICATION_JSON_VALUE)
	public Saludo probarServicio() {
		String Url =  this.urlCte + "/saludo/json/{nombre}";
		
		Saludo saludo =this.restTemplate.getForObject(Url, Saludo.class, "Jimena");
		
		System.out.println("objeto recuperado de " + Url + " es " + saludo);
		return saludo;
	}

	@GetMapping(path = "/testArreglo", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Saludo> probarGetArreglo() {
		
		String Url =  this.urlCte + "/saludos";
		
		Saludo[] saludos =this.restTemplate.getForObject(Url, Saludo[].class);
		
		List<Saludo> listSaludos = Arrays.asList(saludos);		
		System.out.println("objeto recuperado de " + Url + " es " + listSaludos);
		return listSaludos;
	}
	@GetMapping(path = "/testArregloErr", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Saludo> probarGetArregloErr() {
		
		List<Saludo> listSaludos = null;
		String Url =  this.urlCte + "/saludos";
		try {
			Saludo[] saludos =this.restTemplate.getForObject(Url, Saludo[].class);
			listSaludos = Arrays.asList(saludos);		
			System.out.println("objeto recuperado de " + Url + " es " + listSaludos);
		}
		catch(RestClientResponseException rcrex) {
			Saludo saludoErr = new Saludo("error", rcrex.getResponseBodyAsString());
			listSaludos.add(saludoErr);
		}		
		return listSaludos;
	}
	@ExceptionHandler
	public String manejarError(Exception ex) {
		String error = ex.getClass().getName() + ":" + ex.getMessage();
		return error;		
	}
}
