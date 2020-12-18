package curso.apirest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import curso.apirest.apirest.ApplicationContextLoad;
import curso.apirest.model.Usuario;
import curso.apirest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
// @Component - @Service já tem incluso @Component, isto foi comentário de um
// aluno
public class JWTTokenAutenticacaoService {

	/* Tempo para validade do token é de 2 dias - milisegundos */
	private static final long EXPIRATION_TIME = 172800000;

	/* Uma senha única para compor a autenticação */
	/*
	 * Exemplo - SECRET = "4234fe¨%%_-w6jj,j!@$%%%" Ou uma assinatura eletrônica *
	 */
	private static final String SECRET = "AaBbC *Matrix@";

	/* Prefixo padrão */
	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	/* Gerando token de autenticação e adicionado ao cabeçalho e resposta http */
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {

		/* Montagem do token */
		String JWT = Jwts.builder() /* Chama o gerador de token */
				.setSubject(username) /* Add o usuário */
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* Verificar a expíração */
				.signWith(SignatureAlgorithm.HS512, SECRET).compact(); /* compactação do algoritmo */

		/* Junta token com o prefixo - Bearer usidjsnfwhr7r0459045-6hnvnjn (exemplo) */
		String token = TOKEN_PREFIX + " " + JWT;

		/*
		 * Add cabeçalho de resposta - Authorization Bearer usidjsnfwhr7r0459045-6hnvnjn
		 * (exemplo)
		 */
		response.addHeader(HEADER_STRING, token);

		/**Aqui o Token será atualizado */
		ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class).atualizarToken(JWT, username);

		/**
		 * Liberando resposta para portas diferentes que usam API ou caso cliente WEB
		 */
		liberacaoCord(response);

		/* Escreve token como resposta no corpo http */
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}

	/* Retorna o usuario validado com token ou caso não seja valido retorna null */
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

		/* Pega o token no cabeçalho http */
		String token = request.getHeader(HEADER_STRING);

		try {

			if (token != null) {
				/* Pegar token somente */
				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

				/* Faz a validação do token do usuário */
				String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(tokenLimpo).getBody().getSubject();

				if (user != null) {

					Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
							.findByUsuarioByLogin(user);

					/* Retornar o usuario */
					if (usuario != null) {

						/**
						 * Dubla validação para verificar se realmente token que está sendo usado é do
						 * usuário
						 */
						if (tokenLimpo.equals(usuario.getToken())) {

							return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
									usuario.getAuthorities());

						}

					}

				}
			} // fim da veriicação de token está null ou não

		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			try {
				response.getOutputStream().println("Token expirado, favor relogar ou renovar Token");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		liberacaoCord(response);
		return null; // Não autorizado
	}

	private void liberacaoCord(HttpServletResponse response) {

		if (response.getHeader("Access-Control-Allow-Origin") == null) {

			response.addHeader("Access-Control-Allow-Origin", "*");

		}
		if (response.getHeader("Access-Control-Allow-Headers") == null) {

			response.addHeader("Access-Control-Allow-Headers", "*");

		}
		if (response.getHeader("Access-Control-Request-Headers") == null) {

			response.addHeader("Access-Control-Request-Headers", "*");

		}
		if (response.getHeader("Access-Control-Allow-Methods") == null) {

			response.addHeader("Access-Control-Allow-Methods", "*");

		}

	}
}
