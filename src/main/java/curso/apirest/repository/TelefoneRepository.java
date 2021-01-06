package curso.apirest.repository;

import curso.apirest.model.Telefones;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefones, Long> {

    
    
}
