package mx.com.qtx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ClienteEjemploRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClienteEjemploRestApplication.class, args);
	}
	
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
