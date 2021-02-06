package curso.apirest.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

	@Query(value = "select constraint_name from information_schema.constraint_column_usage where table_name = 'usuarios_role' and column_name = 'role_id';", nativeQuery = true)
	String consultaConstraintRole();

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO usuarios_role (usuario_id, role_id) VALUES (?1, (SELECT id FROM role where cargo = 'ROLE_USER'));")
	void acessoPadrao(Long id);


	default Page<Usuario> findUserByNamePage(String nome, PageRequest pageRequest) {
		
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Usuario> example = Example.of(usuario, exampleMatcher);
		
		return findAll(example, pageRequest);
	}



}
