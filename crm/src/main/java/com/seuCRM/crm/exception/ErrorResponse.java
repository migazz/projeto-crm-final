package com.seuCRM.crm.exception;

import java.time.LocalDateTime;
import java.util.List;

// Objeto que representa a resposta de erro padronizada
// Toda vez que algo der errado, a API vai retornar esse formato
public class ErrorResponse {

    private int status;
    private String erro;
    private String mensagem;
    private LocalDateTime timestamp;
    private List<String> detalhes; // usado para erros de validação

    public ErrorResponse(int status, String erro, String mensagem) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String erro, String mensagem, List<String> detalhes) {
        this(status, erro, mensagem);
        this.detalhes = detalhes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(List<String> detalhes) {
        this.detalhes = detalhes;
    }
}