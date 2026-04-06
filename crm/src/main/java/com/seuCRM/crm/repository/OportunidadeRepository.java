package com.seuCRM.crm.repository;

import com.seuCRM.crm.entity.Oportunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {

    // SELECT * FROM oportunidades WHERE cliente_id = ?
    List<Oportunidade> findByClienteId(Long clienteId);

    // SELECT * FROM oportunidades WHERE responsavel_id = ?
    List<Oportunidade> findByResponsavelId(Long responsavelId);

    // SELECT * FROM oportunidades WHERE estagio = ?
    List<Oportunidade> findByEstagio(Oportunidade.Estagio estagio);

    // SELECT * FROM oportunidades WHERE cliente_id = ? AND estagio = ?
    List<Oportunidade> findByClienteIdAndEstagio(Long clienteId, Oportunidade.Estagio estagio);

    // SELECT * FROM oportunidades WHERE titulo LIKE %titulo%
    List<Oportunidade> findByTituloContainingIgnoreCase(String titulo);
}