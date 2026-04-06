package com.seuCRM.crm.dto;

import com.seuCRM.crm.entity.Oportunidade;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OportunidadeDTO {

    private Long id; // Fundamental para o React
    @NotBlank(message = "Título é obrigatório")
    private String titulo;
    private String descricao;
    private BigDecimal valor;
    private Oportunidade.Estagio estagio;
    private LocalDate dataFechamento;
    private Long clienteId;
    private Long responsavelId;

    public Oportunidade toEntity() {
        Oportunidade op = new Oportunidade();
        op.setId(this.id);
        op.setTitulo(this.titulo);
        op.setDescricao(this.descricao);
        op.setValor(this.valor);
        op.setDataFechamento(this.dataFechamento);
        op.setEstagio(this.estagio != null ? this.estagio : Oportunidade.Estagio.PROSPECCAO);
        return op;
    }

    public static OportunidadeDTO fromEntity(Oportunidade op) {
        OportunidadeDTO dto = new OportunidadeDTO();
        dto.setId(op.getId());
        dto.setTitulo(op.getTitulo());
        dto.setDescricao(op.getDescricao());
        dto.setValor(op.getValor());
        dto.setEstagio(op.getEstagio());
        dto.setDataFechamento(op.getDataFechamento());
        if (op.getCliente() != null)
            dto.setClienteId(op.getCliente().getId());
        if (op.getResponsavel() != null)
            dto.setResponsavelId(op.getResponsavel().getId());
        return dto;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Oportunidade.Estagio getEstagio() {
        return estagio;
    }

    public void setEstagio(Oportunidade.Estagio estagio) {
        this.estagio = estagio;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }
}