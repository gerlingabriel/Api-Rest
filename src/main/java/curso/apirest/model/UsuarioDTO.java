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

    private String usuarioLogin;
    private String usuarioNome;

    public UsuarioDTO(Usuario usuario){

        this.usuarioLogin = usuario.getLogin();
        this.usuarioNome = usuario.getNome();

    }


}
