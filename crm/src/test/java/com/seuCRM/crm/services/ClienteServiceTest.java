package com.seuCRM.crm.services;

import com.seuCRM.crm.entity.Cliente;
import com.seuCRM.crm.exception.BusinessException;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// @ExtendWith habilita o Mockito nos testes
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    // @Mock cria um "falso" do repository — não vai ao banco de verdade
    @Mock
    private ClienteRepository clienteRepository;

    // @InjectMocks cria o Service real e injeta os mocks nele
    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteValido;

    // Roda antes de cada teste — monta os dados de exemplo
    @BeforeEach
    void setUp() {
        clienteValido = new Cliente();
        clienteValido.setId(1L);
        clienteValido.setNome("Empresa Teste");
        clienteValido.setCnpj("12.345.678/0001-99");
        clienteValido.setEmail("teste@empresa.com");
        clienteValido.setStatus(Cliente.Status.ATIVO);
    }

    @Test
    @DisplayName("Deve listar todos os clientes com sucesso")
    void deveListarTodosOsClientes() {
        // Arrange — configura o que o mock deve retornar
        when(clienteRepository.findAll()).thenReturn(List.of(clienteValido));

        // Act — executa o método que queremos testar
        List<Cliente> resultado = clienteService.listarTodos();

        // Assert — verifica se o resultado é o esperado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Empresa Teste", resultado.get(0).getNome());

        // Verifica que o repository foi chamado exatamente 1 vez
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void deveBuscarClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Empresa Teste", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve criar cliente com sucesso")
    void deveCriarClienteComSucesso() {
        // Simula que o CNPJ não existe ainda
        when(clienteRepository.existsByCnpj(anyString())).thenReturn(false);
        // Simula o save retornando o cliente com ID
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteValido);

        Cliente resultado = clienteService.criar(clienteValido);

        assertNotNull(resultado);
        assertEquals("Empresa Teste", resultado.getNome());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cliente com CNPJ duplicado")
    void deveLancarExcecaoComCnpjDuplicado() {
        // Simula que o CNPJ JÁ existe no banco
        when(clienteRepository.existsByCnpj(anyString())).thenReturn(true);

        // Verifica que a exceção é lançada
        BusinessException excecao = assertThrows(
                BusinessException.class,
                () -> clienteService.criar(clienteValido));

        assertEquals("CNPJ já cadastrado: 12.345.678/0001-99", excecao.getMessage());
        // Verifica que o save NUNCA foi chamado
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar cliente inexistente")
    void deveLancarExcecaoAoDeletarClienteInexistente() {
        when(clienteRepository.existsById(999L)).thenReturn(false);

        ResourceNotFoundException excecao = assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.deletar(999L));

        assertTrue(excecao.getMessage().contains("999"));
        verify(clienteRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve deletar cliente com sucesso")
    void deveDeletarClienteComSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        assertDoesNotThrow(() -> clienteService.deletar(1L));
        verify(clienteRepository, times(1)).deleteById(1L);
    }
}