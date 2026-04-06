package com.seuCRM.crm.services;

import com.seuCRM.crm.entity.Cliente;
import com.seuCRM.crm.entity.Contato;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.repository.ClienteRepository;
import com.seuCRM.crm.repository.ContatoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final ClienteRepository clienteRepository;

    public ContatoService(ContatoRepository contatoRepository,
            ClienteRepository clienteRepository) {
        this.contatoRepository = contatoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Contato> listarTodos() {
        return contatoRepository.findAll();
    }

    public List<Contato> listarPorCliente(Long clienteId) {
        return contatoRepository.findByClienteId(clienteId);
    }

    public Optional<Contato> buscarPorId(Long id) {
        return contatoRepository.findById(id);
    }

    public Contato criar(Long clienteId, Contato contato) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
        contato.setCliente(cliente);
        return contatoRepository.save(contato);
    }

    public Contato atualizar(Long id, Contato dadosNovos) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato", id));
        contato.setNome(dadosNovos.getNome());
        contato.setEmail(dadosNovos.getEmail());
        contato.setTelefone(dadosNovos.getTelefone());
        contato.setCargo(dadosNovos.getCargo());
        return contatoRepository.save(contato);
    }

    public void deletar(Long id) {
        if (!contatoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contato", id);
        }
        contatoRepository.deleteById(id);
    }
}