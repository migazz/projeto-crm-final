package com.seuCRM.crm.repository;

import com.seuCRM.crm.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    // SELECT * FROM contatos WHERE cliente_id = ?
    // Busca todos os contatos de um cliente específico
    List<Contato> findByClienteId(Long clienteId);

    // SELECT * FROM contatos WHERE nome LIKE %nome%
    List<Contato> findByNomeContainingIgnoreCase(String nome);

    // SELECT * FROM contatos WHERE email = ?
    List<Contato> findByEmail(String email);
}