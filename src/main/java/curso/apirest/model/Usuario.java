package curso.apirest.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario implements UserDetails, Comparable<Usuario> {

	/*
	 * @Data - coloca todos settes, gettes, ToString, EquilsHash, tudo
	 * 
	 * @EqualsAndHashCode(exclude={"firstName", "lastName", "gender"}) deve excluir
	 * todos que não são importantes
	 * 
	 * @ToString(exclude="id")
	 * 
	 * @Builder - não deixa modoficar os valores, assim não é criado os settes e
	 * gettes
	 * 
	 * @Slf4j - subtitua @log e @CommonsLog. (algum relacioado a log de registro)
	 * 
	 * @NoArgsConstructor - contrutor vazio
	 * 
	 * @AllArgsConstructor - contrutor com todos elementos
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 50, unique = true)
	private String login;

	private String senha;

	private String token = "";

	@Column(length = 50)
	private String nome;

	@Column(length = 9)
	private String cep;

	@Column(length = 60)
	private String logradouro;

	@Column(length = 30)
	private String complemento;

	@Column(length = 50)
	private String bairro;

	@Column(length = 50)
	private String localidade;

	@Column(length = 2)
	private String uf;

	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Telefones> telefones = new ArrayList<Telefones>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinTable(name = "usuarios_role", joinColumns = {
			@JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "usuario", unique = false) },
			/** Criação da primeira chave */

			inverseJoinColumns = {
					@JoinColumn(name = "role_id", referencedColumnName = "id", table = "role", unique = false) })
	private List<Role> roles = new ArrayList<Role>();

	/*
	 * *** METODOS PAR CONTROLE DE ACESSO DO SPRING E USADO TAMBÉM NO REST
	 ******/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return this.senha;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return this.login;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
	/* **************** FIM DOS METÓDOS ************************/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Telefones> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefones> telefones) {
		this.telefones = telefones;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Usuario o) {
		return Long.compare(this.id, o.id);
	}

	public String getCep() {
		return cep;
	}

	public String getBairro() {
		return bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getLocalidade() {
		return localidade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getToken() {
		return token;
	}

	public String getUf() {
		return uf;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
