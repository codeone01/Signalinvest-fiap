package br.com.signalinvest.api;

import br.com.signalinvest.exception.ConflitoException;
import br.com.signalinvest.exception.NaoAutorizadoException;
import br.com.signalinvest.exception.RecursoNaoEncontradoException;
import br.com.signalinvest.exception.RegraNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class TratadorErros {
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ApiErro> tratarRegra(RegraNegocioException erro, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, erro.getMessage(), request);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiErro> tratarRequisicaoInvalida(Exception erro, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, "A requisição contém dados inválidos.", request);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErro> tratarNaoEncontrado(RecursoNaoEncontradoException erro, HttpServletRequest request) {
        return resposta(HttpStatus.NOT_FOUND, erro.getMessage(), request);
    }

    @ExceptionHandler(NaoAutorizadoException.class)
    public ResponseEntity<ApiErro> tratarNaoAutorizado(NaoAutorizadoException erro, HttpServletRequest request) {
        return resposta(HttpStatus.UNAUTHORIZED, erro.getMessage(), request);
    }

    @ExceptionHandler({ConflitoException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiErro> tratarConflito(Exception erro, HttpServletRequest request) {
        String mensagem = erro instanceof ConflitoException
                ? erro.getMessage()
                : "A operação conflita com dados relacionados já existentes.";
        return resposta(HttpStatus.CONFLICT, mensagem, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErro> tratarInesperado(Exception erro, HttpServletRequest request) {
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Não foi possível concluir a operação. Tente novamente.", request);
    }

    private ResponseEntity<ApiErro> resposta(HttpStatus status, String mensagem, HttpServletRequest request) {
        ApiErro corpo = new ApiErro(OffsetDateTime.now(), status.value(), status.getReasonPhrase(),
                mensagem, request.getRequestURI());
        return ResponseEntity.status(status).body(corpo);
    }
}
