package curso.apirest.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioDTO implements Serializable, Comparable<UsuarioDTO> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String login;
    private String nome;
    private String senha;

    public UsuarioDTO(Usuario usuario){

        this.login = usuario.getLogin();
        this.nome = usuario.getNome();
        this.id = usuario.getId();
        this.senha = usuario.getSenha();

    }

    @Override
	public int compareTo(UsuarioDTO o) {
		return Long.compare(this.id, o.id);
	}


}
