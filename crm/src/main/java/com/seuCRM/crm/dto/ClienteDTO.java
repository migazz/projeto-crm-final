package com.seuCRM.crm.dto;

import com.seuCRM.crm.entity.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClienteDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;
    private String cidade;

    @Size(max = 2, message = "Estado deve ter 2 letras (ex: SP)")
    private String estado;

    private Cliente.Status status;

    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setId(this.id); // Fundamental para o UPDATE funcionar
        cliente.setNome(this.nome);
        cliente.setCnpj(this.cnpj);
        cliente.setEmail(this.email);
        cliente.setTelefone(this.telefone);
        cliente.setEndereco(this.endereco);
        cliente.setCidade(this.cidade);
        cliente.setEstado(this.estado);
        cliente.setStatus(this.status != null ? this.status : Cliente.Status.ATIVO);
        return cliente;
    }

    public static ClienteDTO fromEntity(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setCnpj(cliente.getCnpj());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setEndereco(cliente.getEndereco());
        dto.setCidade(cliente.getCidade());
        dto.setEstado(cliente.getEstado());
        dto.setStatus(cliente.getStatus());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente.Status getStatus() {
        return status;
    }

    public void setStatus(Cliente.Status status) {
        this.status = status;
    }
}