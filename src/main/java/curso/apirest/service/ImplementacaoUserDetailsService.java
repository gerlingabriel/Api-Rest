package curso.apirest.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import curso.apirest.model.Usuario;
import curso.apirest.repository.UsuarioRepository;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JdbcTemplate jdbcTempalte;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*Consultar o banco*/		
		Usuario usu = usuarioRepository.findByUsuarioByLogin(username);
		
		if(usu == null) {
			
			throw new UsernameNotFoundException("Usuario não encontrado");			
			
		}

		return new User(usu.getLogin(), usu.getSenha(), usu.getAuthorities());
	}

	public void inserirRoles(Long id){

		String constraint = usuarioRepository.consultaConstraintRole();

		if(constraint != null){
			jdbcTempalte.execute(" ALTER TABLE usuarios_role DROP CONSTRAINT " + constraint);
		}

		usuarioRepository.acessoPadrao(id);

	}

	public void deletarRoles(Long id){

		String constraint = usuarioRepository.consultaConstraintRole();

		if(constraint != null){
			jdbcTempalte.execute(" ALTER TABLE usuarios_role DROP CONSTRAINT " + constraint);
		}
		
		jdbcTempalte.execute("DELETE FROM usuarios_role WHERE usuario_id = " + id);

	}

}
