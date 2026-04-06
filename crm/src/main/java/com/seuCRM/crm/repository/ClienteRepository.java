package com.seuCRM.crm.repository;

import com.seuCRM.crm.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // SELECT * FROM clientes WHERE nome LIKE %nome%
    // IgnoreCase = ignora maiúsculas/minúsculas na busca
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    // SELECT * FROM clientes WHERE cnpj = ?
    Optional<Cliente> findByCnpj(String cnpj);

    // SELECT * FROM clientes WHERE status = ?
    List<Cliente> findByStatus(Cliente.Status status);

    // SELECT * FROM clientes WHERE cidade = ?
    List<Cliente> findByCidade(String cidade);

    // SELECT * FROM clientes WHERE cnpj = ? (retorna true/false)
    boolean existsByCnpj(String cnpj);
}
