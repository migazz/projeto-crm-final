package com.seuCRM.crm.services;

import com.seuCRM.crm.entity.Cliente;
import com.seuCRM.crm.exception.BusinessException;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Cliente> buscarPorStatus(Cliente.Status status) {
        return clienteRepository.findByStatus(status);
    }

    public Cliente criar(Cliente cliente) {
        if (cliente.getCnpj() != null && !cliente.getCnpj().isBlank()) {
            if (clienteRepository.existsByCnpj(cliente.getCnpj())) {
                throw new BusinessException("CNPJ já cadastrado: " + cliente.getCnpj());
            }
        }
        if (cliente.getStatus() == null) {
            cliente.setStatus(Cliente.Status.ATIVO);
        }
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente dadosNovos) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));

        cliente.setNome(dadosNovos.getNome());
        cliente.setEmail(dadosNovos.getEmail());
        cliente.setTelefone(dadosNovos.getTelefone());
        cliente.setEndereco(dadosNovos.getEndereco());
        cliente.setCidade(dadosNovos.getCidade());
        cliente.setEstado(dadosNovos.getEstado());

        if (dadosNovos.getStatus() != null) {
            cliente.setStatus(dadosNovos.getStatus());
        }
        if (dadosNovos.getCnpj() != null && !dadosNovos.getCnpj().equals(cliente.getCnpj())) {
            if (clienteRepository.existsByCnpj(dadosNovos.getCnpj())) {
                throw new BusinessException("CNPJ já em uso: " + dadosNovos.getCnpj());
            }
            cliente.setCnpj(dadosNovos.getCnpj());
        }
        return clienteRepository.save(cliente);
    }

    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }
}