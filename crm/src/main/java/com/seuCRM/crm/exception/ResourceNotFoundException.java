package com.seuCRM.crm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) // Adicione esta linha!
public class ResourceNotFoundException extends RuntimeException {

    private final String recurso;
    private final Object id;

    public ResourceNotFoundException(String recurso, Object id) {
        super(recurso + " não encontrado com id: " + id);
        this.recurso = recurso;
        this.id = id;
    }

    public String getRecurso() {
        return recurso;
    }

    public Object getId() {
        return id;
    }
}