package curso.apirest.apirest;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ObjetoError {

    private String error;
    private String code;
    
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
    
    
}
