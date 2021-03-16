package curso.apirest.apirest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/** Criado para pegar instancia, pois curso falou que não estava conseguindo pegar instacio no UsuarioRepository Direto */
@Component
public class ApplicationContextLoad implements ApplicationContextAware{
	
	@Autowired
	private static ApplicationContext applicationContext; /** ApplicationContext - interface do Sptring Boot */

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {		
		ApplicationContextLoad.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}

	