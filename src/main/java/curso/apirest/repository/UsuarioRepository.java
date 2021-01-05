package curso.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.apirest.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	@Query("select u from  Usuario u where u.login = ?1")
	Usuario findByUsuarioByLogin(String login);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update usuario set token =?1 where login = ?2")
	void atualizarToken(String token, String login);

	@Query("select u from  Usuario u where u.nome like %?1%")
	List<Usuario> findByUsuarioByNome(String nome);


}
