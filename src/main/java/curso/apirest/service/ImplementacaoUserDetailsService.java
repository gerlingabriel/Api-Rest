package curso.apirest.service;


import org.springframework.beans.factory.annotation.Autowired;
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*Consultar o banco*/		
		Usuario usu = usuarioRepository.findByUsuarioByLogin(username);
		
		if(usu == null) {
			
			throw new UsernameNotFoundException("Usuario não encontrado");			
			
		}

		return new User(usu.getLogin(), usu.getSenha(), usu.getAuthorities());
	}

}
