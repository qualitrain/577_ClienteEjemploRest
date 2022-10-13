package mx.com.qtx.web;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

import mx.com.qtx.entidades.Saludo;

@RestController
public class ApiWeb {
	
	private static Logger log = LoggerFactory.getLogger(ApiWeb.class);
	private static int nPeticion = 0;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private EurekaClient eurekaCte;
	
	@Autowired
	private Environment env;
	
	public ApiWeb(){
		log.info("ApiWeb()");
	}
	
	private String getUrlCte(){
		String urlCte = "http://";
		String idServicioCte = env.getProperty("mx.com.qtx.servicio01");
//		Application aplicacion = this.eurekaCte.getApplication(idServicioCte);
//		List<InstanceInfo> instanciasApp = aplicacion.getInstances();
//		if(instanciasApp.size() == 0) {
//			log.error("No hay instancias disponibles del servicio " + idServicioCte);
//			return null;
//		}
//		InstanceInfo instanciaDestino = instanciasApp.get(0);
		urlCte += idServicioCte;
		return urlCte;
	}
	
	@GetMapping(path = "/testServicio", produces = MediaType.APPLICATION_JSON_VALUE)
	public Saludo probarServicio() {
		String Url =  this.getUrlCte() + "/saludo/json/{nombre}";
		
		Saludo saludo =this.restTemplate.getForObject(Url, Saludo.class, "Jimena");
		
		nPeticion++;
		System.out.print("(" + nPeticion + ") ");
		System.out.println("objeto recuperado de " + Url + " es " + saludo);
		return saludo;
	}

	@GetMapping(path = "/testArreglo", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Saludo> probarGetArreglo() {
		
		String Url =  this.getUrlCte() + "/saludos";
		
		Saludo[] saludos =this.restTemplate.getForObject(Url, Saludo[].class);
		
		List<Saludo> listSaludos = Arrays.asList(saludos);
		
		nPeticion++;
		System.out.print("(" + nPeticion + ") ");
		System.out.println("objeto recuperado de " + Url + " es " + listSaludos);
		return listSaludos;
	}
	
	@GetMapping(path = "/testArregloErr", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Saludo> probarGetArregloErr() {
		
		List<Saludo> listSaludos = null;
		String Url =  this.getUrlCte() + "/saludos";
		try {
			Saludo[] saludos =this.restTemplate.getForObject(Url, Saludo[].class);
			listSaludos = Arrays.asList(saludos);		
			nPeticion++;
			System.out.print("(" + nPeticion + ") ");
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
