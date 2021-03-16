package curso.apirest.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import curso.apirest.service.ImplementacaoUserDetailsService;

/*mapear acessos a URL (acesso e bloqueio)*/
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService detailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		/* Service que irá consultar o usuário no banco de dados */
		/* passwordEncoder é a criptação da senha */
		auth.userDetailsService(detailsService).passwordEncoder(new BCryptPasswordEncoder());

	}

	@Override /*Configura as requisições de http*/
	protected void configure(HttpSecurity http) throws Exception {
		/** Dica na internet por causa dos erros relacionado ao CORS */
		http.cors().and()

		/*Ativando a proteção contra usuarios que não estão validados por token*/
		.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*permitindo ao acesso a pagina incial do projeto Ex. projeto.com.br/ */
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll() /*Se tiver uma pagina index também ppermitir acesso*/

		/** Permite que varias clientes com portas diferentes, servidores diferetnes possam utilizar o serviços (será preciso quando for usar ANGULA) */
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		/*URL de logout - redireciona apos  o user deslogar*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/*Mapea a URL de logout e invalida o usuário*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/*Filtrar requisições de login para autenticação*/
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		
		/*Filtra demais requisições para verificar a presença do token JWT no heat Http*/
		.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
	}

	@Bean
    CorsConfigurationSource corsConfigurationSource(){

		CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowedOrigins(Arrays.asList("http://localhost:8087/", "http://localhost:8080/", "http://localhost:4200/", "http://localhost:4200"));
		
		config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("*"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		source.registerCorsConfiguration("/**", config);
		
        return source;
    }
	

}
