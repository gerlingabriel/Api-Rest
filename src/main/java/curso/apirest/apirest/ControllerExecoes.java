package curso.apirest.apirest;

import java.sql.SQLException;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestControllerAdvice
public class ControllerExecoes extends ResponseEntityExceptionHandler {

    /** Metodo que trata maiorida dos erros */
    @Override
    @ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ObjetoError error = new ObjetoError();
        error.setError(ex.getMessage());
        error.setCode(status.value() + " ==> " + status.getReasonPhrase());

        return new ResponseEntity<>(error, headers, status);
    }

    /** Tratamento de erro para maioria dos problemas no banco de dados */
    @ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, PSQLException.class, SQLException.class })
    protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {

        String msg = "";

        if (ex instanceof DataIntegrityViolationException) {
            msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
        } else if (ex instanceof ConstraintViolationException) {
            msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();
        } else if (ex instanceof PSQLException) {
            msg = ((PSQLException) ex).getCause().getCause().getMessage();
        } else if (ex instanceof SQLException) {
            msg = ((SQLException) ex).getCause().getCause().getMessage();
        } else {
            msg = ex.getMessage();
        }

        ObjetoError error = new ObjetoError();
        error.setError(msg);
        error.setCode(HttpStatus.INTERNAL_SERVER_ERROR + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
