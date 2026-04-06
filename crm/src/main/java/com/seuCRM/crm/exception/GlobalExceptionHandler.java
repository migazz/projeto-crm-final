package com.seuCRM.crm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(
                        ResourceNotFoundException ex) {
                ErrorResponse erro = new ErrorResponse(
                                404, "Recurso não encontrado", ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusinessException(
                        BusinessException ex) {
                ErrorResponse erro = new ErrorResponse(
                                400, "Erro de negócio", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationErrors(
                        MethodArgumentNotValidException ex) {
                List<String> detalhes = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.toList());
                ErrorResponse erro = new ErrorResponse(
                                400, "Erro de validação",
                                "Um ou mais campos estão inválidos", detalhes);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentials(
                        BadCredentialsException ex) {
                ErrorResponse erro = new ErrorResponse(
                                401, "Não autorizado", "Email ou senha incorretos");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
        }

        // Deixa o AuthenticationException passar para o AuthController tratar
        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ErrorResponse> handleAuthentication(
                        AuthenticationException ex) {
                ErrorResponse erro = new ErrorResponse(
                                401, "Não autorizado", "Email ou senha incorretos");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDenied(
                        AccessDeniedException ex) {
                ErrorResponse erro = new ErrorResponse(
                                403, "Acesso negado",
                                "Você não tem permissão para acessar este recurso");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
        }

        // Genérico — só captura o que não foi tratado acima
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
                // Loga o erro real para debug
                ex.printStackTrace();
                ErrorResponse erro = new ErrorResponse(
                                500, "Erro interno",
                                "Ocorreu um erro inesperado. Tente novamente mais tarde.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
}