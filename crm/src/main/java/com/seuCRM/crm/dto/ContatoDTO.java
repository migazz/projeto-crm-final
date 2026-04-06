package com.seuCRM.crm.dto;

import com.seuCRM.crm.entity.Contato;
import jakarta.validation.constraints.NotBlank;

public class ContatoDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String email;
    private String telefone;
    private String cargo;

    public Contato toEntity() {
        Contato contato = new Contato();
        contato.setNome(this.nome);
        contato.setEmail(this.email);
        contato.setTelefone(this.telefone);
        contato.setCargo(this.cargo);
        return contato;
    }

    public static ContatoDTO fromEntity(Contato contato) {
        ContatoDTO dto = new ContatoDTO();
        dto.setNome(contato.getNome());
        dto.setEmail(contato.getEmail());
        dto.setTelefone(contato.getTelefone());
        dto.setCargo(contato.getCargo());
        return dto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}