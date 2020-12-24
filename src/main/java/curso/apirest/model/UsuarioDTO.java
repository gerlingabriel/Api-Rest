package curso.apirest.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String login;
    private String nome;

    public UsuarioDTO(Usuario usuario){

        this.login = usuario.getLogin();
        this.nome = usuario.getNome();
        this.id = usuario.getId();

    }


}
