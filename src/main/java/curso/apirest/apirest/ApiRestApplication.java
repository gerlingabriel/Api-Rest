package curso.apirest.apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EntityScan(basePackages = {"curso.apirest.model"})
@ComponentScan(basePackages = {"curso.*"})
@EnableJpaRepositories(basePackages = {"curso.apirest.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration /*talvez essa anotação está vinculado ao SptringBootApplication*/
@EnableCaching
public class ApiRestApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(ApiRestApplication.class, args);

		//System.out.println(new BCryptPasswordEncoder().encode("123")); //para testar a crip*/
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/**").allowedMethods("POST", "GET", "PUT", "DELETE", "*").allowedOrigins("*").allowedHeaders("*").allowCredentials(false);
			
		/* addMapping vai liberar os controles
		 * addMapping("/**") - liberar o sistema todo
		 * 
		 * allowedMethods vai liberar os metodos, POST, GET, PUT
		 * allowedMethods("POST", "DELETE") - libera somente esse dois metodos dentro
		 * do controller acima
		 * 
		 * allowedOrigins("*") - controlle da onde vai vir os ajax autorizados
		 * allowedOrigins("www.cliente20.com.br")
		 * allowedOrigins("www.cliente20.com.br", "www.cliente40.com.br")
		 * */
	}

		
}
