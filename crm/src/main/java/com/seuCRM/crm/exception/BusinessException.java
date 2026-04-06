package com.seuCRM.crm.exception;

// Exceção lançada quando uma regra de negócio é violada
// Exemplo: tentar cadastrar CNPJ duplicado
public class BusinessException extends RuntimeException {

    public BusinessException(String mensagem) {
        super(mensagem);
    }
}