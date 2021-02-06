package curso.apirest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;

import curso.apirest.model.Usuario;
import curso.apirest.repository.TelefoneRepository;
import curso.apirest.repository.UsuarioRepository;
import curso.apirest.service.ImplementacaoUserDetailsService;

@RestController /* Arquiterura REST */
@RequestMapping("/usuario")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IndexController {

	@Autowired /***** Se estivesse usando CDI teria que usar @Inject *****/
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	@Autowired
	private TelefoneRepository telefoneRepository;

	/* Serviço RESTful */
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {
		/**
		 * usa-se uma ourta classe para esconder os atributos e mostra somente o que
		 * metodo "UsuarioDTO" quer mostrar
		 */

		Usuario usu = usuarioRepository.findById(id).get();

		return new ResponseEntity<Usuario>(usu, HttpStatus.OK);
	}

	/* Serviço RESTful */
	@GetMapping(value = "/", produces = "application/json")
	@CachePut(value = "cachelista")
	public ResponseEntity<Page<Usuario>> lista() throws InterruptedException {

		/* Simular um processo longe e demorado */
		/* Thread.sleep(8000); */

		/**Rotina para paginação de acordo com Angular */
		PageRequest page = PageRequest.of(0, 5, Sort.by("id"));
		Page<Usuario> list = usuarioRepository.findAll(page);

		//Collections.sort(list); /** Passar para Angular a lista ordenada por ID */

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	@CachePut(value = "cachelista")
	public ResponseEntity<Page<Usuario>> listaPAginacao(@PathVariable(value = "pagina") int pagina) throws InterruptedException {

		/* Simular um processo longe e demorado */
		/* Thread.sleep(8000); */

		/**Rotina para paginação de acordo com Angular */
		PageRequest page = PageRequest.of(pagina, 5, Sort.by("id"));
		Page<Usuario> list = usuarioRepository.findAll(page);

		//Collections.sort(list); /** Passar para Angular a lista ordenada por ID */

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}

	@PostMapping("/")
	@CacheEvict(value = "cachelista", allEntries = true)
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws Exception {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}

		if (usuario.getCep() != null) {

			/** Consumindo API publica externa */

			URL url = new URL("https://viacep.com.br/ws/" + usuario.getCep() + "/json/");
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream(); // verá os dados que requisições que acessei
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String cep = "";
			StringBuffer jsonCep = new StringBuffer();

			while ((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}

			Usuario auxUsuario = new Gson().fromJson(jsonCep.toString(), Usuario.class);

			usuario.setLogradouro(auxUsuario.getLogradouro());
			usuario.setComplemento(auxUsuario.getComplemento());
			usuario.setLocalidade(auxUsuario.getLocalidade());
			usuario.setBairro(auxUsuario.getBairro());
			usuario.setUf(auxUsuario.getUf());

				/** Fim do consumo API do cep */

		}
	

		/** Criptografar a senha para segurnaça do sistema */
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));

		/** Fica salva no banco de dados a senha criptografado */
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		implementacaoUserDetailsService.inserirRoles(usuario.getId());

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> update(@RequestBody Usuario usuario) {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		Usuario aux = usuarioRepository.findById(usuario.getId()).get();

		/*
		 * Caso a senha for diferente(editado) então terei que pegar a senha e
		 * criptografar
		 */
		if (!usuario.getSenha().equals(aux.getSenha())) { // equal pois são Strings
			usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		}

		Usuario novoUsuario = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(novoUsuario, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> deletar(@PathVariable(value = "id") Long id) {

		implementacaoUserDetailsService.deletarRoles(id);

		usuarioRepository.deleteById(id);

		return new ResponseEntity<Usuario>(HttpStatus.OK);
	}

	/*
	 * @RequestMaping("/usuario")
	 * 
	 * //ABriria outra pagina utilizando get com controle acima "usuario"
	 * 
	 * @GetMapping(value = "/{codico}", produces = "application/json") public
	 * ResponseEntity init() { return new ResponseEntity("Olá Rest String Boot",
	 * HttpStatus.OK); }
	 */

	/*END-POINT consulta de usuário por nome*/
	@GetMapping(value = "/usuarioPorNome/{nome}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> usuarioPorNome (@PathVariable("nome") String nome) throws InterruptedException{
		
		
		PageRequest pageRequest = null;
		Page<Usuario> list = null;
		
		if (nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {/*Não informou nome*/
			
			pageRequest = PageRequest.of(0, 5, Sort.by("id"));
			list =  usuarioRepository.findAll(pageRequest);
		}else {
			pageRequest = PageRequest.of(0, 5, Sort.by("id"));
			list = usuarioRepository.findUserByNamePage(nome, pageRequest);
		}		
	
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);

	}
	
	
	
	/*END-POINT consulta de usuário por nome*/
	@GetMapping(value = "/usuarioPorNome/{nome}/page/{page}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> usuarioPorNomePage (@PathVariable("nome") String nome, @PathVariable("page") int page) throws InterruptedException{
		
		PageRequest pageRequest = null;
		Page<Usuario> list = null;
		
		if (nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {/*Não informou nome*/
			
			pageRequest = PageRequest.of(page, 5, Sort.by("id"));
			list =  usuarioRepository.findAll(pageRequest);
		}else {
			pageRequest = PageRequest.of(page, 5, Sort.by("id"));
			list = usuarioRepository.findUserByNamePage(nome, pageRequest);
		}
		
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}

	/**Métodod para deletar */
	@DeleteMapping(value = "/removerTelefone/{id}", produces = "application/text")
	public String deletarTelefone(@PathVariable(value = "id") Long id){

		telefoneRepository.deleteById(id);

		return "ok";

	}

}
