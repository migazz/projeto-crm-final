package com.seuCRM.crm.repository;

import com.seuCRM.crm.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// @Repository marca essa interface como componente de acesso a dados
// JpaRepository<Usuario, Long> já vem com os métodos prontos:
//   save(), findById(), findAll(), deleteById(), count(), existsById()...
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // O Spring lê o nome do método e gera o SQL automaticamente:
    // SELECT * FROM usuarios WHERE email = ?
    Optional<Usuario> findByEmail(String email);

    // SELECT * FROM usuarios WHERE email = ? (retorna true/false)
    boolean existsByEmail(String email);
}
