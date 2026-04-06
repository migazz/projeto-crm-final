package com.seuCRM.crm.services;

import com.seuCRM.crm.entity.Cliente;
import com.seuCRM.crm.entity.Oportunidade;
import com.seuCRM.crm.entity.Usuario;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.repository.ClienteRepository;
import com.seuCRM.crm.repository.OportunidadeRepository;
import com.seuCRM.crm.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OportunidadeService {

    private final OportunidadeRepository repository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public OportunidadeService(OportunidadeRepository repository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Oportunidade> listarTodas() {
        return repository.findAll();
    }

    public List<Oportunidade> listarPorCliente(Long id) {
        return repository.findByClienteId(id);
    }

    public List<Oportunidade> listarPorEstagio(Oportunidade.Estagio e) {
        return repository.findByEstagio(e);
    }

    public Optional<Oportunidade> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Oportunidade criar(Long clienteId, Oportunidade op, Long responsavelId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
        op.setCliente(cliente);

        if (responsavelId != null) {
            Usuario responsavel = usuarioRepository.findById(responsavelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", responsavelId));
            op.setResponsavel(responsavel);
        }

        if (op.getEstagio() == null) {
            op.setEstagio(Oportunidade.Estagio.PROSPECCAO);
        }

        return repository.save(op);
    }

    @Transactional
    public Oportunidade atualizar(Long id, Oportunidade novos, Long clienteId) {
        Oportunidade existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oportunidade", id));

        existente.setTitulo(novos.getTitulo());
        existente.setDescricao(novos.getDescricao());
        existente.setValor(novos.getValor());
        existente.setDataFechamento(novos.getDataFechamento());

        if (novos.getEstagio() != null) {
            existente.setEstagio(novos.getEstagio());
        }

        if (clienteId != null) {
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
            existente.setCliente(cliente);
        }

        return repository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Oportunidade", id);
        }
        repository.deleteById(id);
    }
}