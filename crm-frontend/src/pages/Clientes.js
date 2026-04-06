import React, { useState, useEffect } from 'react';
import api from '../services/api';
import './Clientes.css';

const Clientes = () => {
    const [clientes, setClientes] = useState([]);
    const [busca, setBusca] = useState('');
    const [modalAberto, setModalAberto] = useState(false);
    const [editandoId, setEditandoId] = useState(null);
    const [novoCliente, setNovoCliente] = useState({
        nome: '', cnpj: '', email: '', telefone: '',
        endereco: '', cidade: '', estado: '', status: 'ATIVO'
    });

    useEffect(() => { carregarClientes(); }, []);

    const carregarClientes = () => {
        api.get('/api/clientes')
            .then(res => setClientes(res.data))
            .catch(err => console.error("Erro:", err));
    };

    const clientesFiltrados = clientes.filter(c =>
        c.nome?.toLowerCase().includes(busca.toLowerCase()) ||
        c.email?.toLowerCase().includes(busca.toLowerCase()) ||
        c.cidade?.toLowerCase().includes(busca.toLowerCase()) ||
        c.cnpj?.includes(busca)
    );

    const maskCNPJ = (v) => {
        if (!v) return "";
        return v.replace(/\D/g, "")
            .replace(/^(\d{2})(\d)/, "$1.$2")
            .replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3")
            .replace(/\.(\d{3})(\d)/, ".$1/$2")
            .replace(/(\d{4})(\d)/, "$1-$2")
            .substring(0, 18);
    };

    const maskTelefone = (v) => {
        if (!v) return "";
        return v.replace(/\D/g, "")
            .replace(/^(\d{2})(\d)/, "($1) $2")
            .replace(/(\d{5})(\d)/, "$1-$2")
            .substring(0, 15);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        let val = value;
        if (name === 'cnpj') val = maskCNPJ(value);
        if (name === 'telefone') val = maskTelefone(value);
        setNovoCliente({ ...novoCliente, [name]: val });
    };

    const abrirModalNovo = () => {
        setEditandoId(null);
        setNovoCliente({
            nome: '', cnpj: '', email: '', telefone: '',
            endereco: '', cidade: '', estado: '', status: 'ATIVO'
        });
        setModalAberto(true);
    };

    const prepararEdicao = (cliente) => {
        setEditandoId(cliente.id);
        setNovoCliente({
            nome: cliente.nome || '',
            cnpj: maskCNPJ(cliente.cnpj || ''),
            email: cliente.email || '',
            telefone: maskTelefone(cliente.telefone || ''),
            endereco: cliente.endereco || '',
            cidade: cliente.cidade || '',
            estado: cliente.estado || '',
            status: cliente.status || 'ATIVO'
        });
        setModalAberto(true);
    };

    const salvarCliente = async (e) => {
        e.preventDefault();
        try {
            const dados = {
                ...novoCliente,
                cnpj: String(novoCliente.cnpj).replace(/\D/g, ""),
                telefone: String(novoCliente.telefone).replace(/\D/g, "")
            };
            if (editandoId) {
                await api.put(`/api/clientes/${editandoId}`, dados);
            } else {
                await api.post('/api/clientes', dados);
            }
            setModalAberto(false);
            setEditandoId(null);
            carregarClientes();
        } catch (err) {
            console.error("Erro:", err.response?.data || err.message);
            alert("Erro ao salvar cliente. Verifique os dados.");
        }
    };

    const excluirCliente = async (id) => {
        if (window.confirm("Deseja excluir este cliente?")) {
            try {
                await api.delete(`/api/clientes/${id}`);
                carregarClientes();
            } catch (err) {
                console.error("Erro ao excluir:", err);
            }
        }
    };

    return (
        <div style={{ width: '100%', padding: '20px' }}>
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                marginBottom: '20px',
                flexWrap: 'wrap',
                gap: '12px'
            }}>
                <h1 style={{ margin: 0, color: '#333' }}>👥 Gestão de Clientes</h1>
                <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
                    {/* Campo de busca */}
                    <input
                        type="text"
                        placeholder="🔍 Buscar por nome, email, cidade..."
                        value={busca}
                        onChange={(e) => setBusca(e.target.value)}
                        style={{
                            padding: '10px 16px',
                            borderRadius: '8px',
                            border: '1.5px solid #e0e0e0',
                            fontSize: '14px',
                            width: '280px',
                            outline: 'none'
                        }}
                    />
                    <button
                        onClick={abrirModalNovo}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#2ecc71',
                            color: 'white',
                            border: 'none',
                            borderRadius: '8px',
                            cursor: 'pointer',
                            fontWeight: 'bold',
                            fontSize: '14px'
                        }}>
                        + Novo Cliente
                    </button>
                </div>
            </div>

            {/* Contador de resultados */}
            {busca && (
                <p style={{ color: '#888', marginBottom: '12px', fontSize: '14px' }}>
                    {clientesFiltrados.length} resultado(s) para "{busca}"
                </p>
            )}

            <div style={{
                backgroundColor: '#fff',
                borderRadius: '12px',
                boxShadow: '0 4px 20px rgba(0,0,0,0.08)',
                overflow: 'hidden'
            }}>
                <div style={{ width: '100%', overflowX: 'auto' }}>
                    <table style={{
                        width: '100%',
                        borderCollapse: 'collapse',
                        minWidth: '800px'
                    }}>
                        <thead>
                            <tr style={{
                                backgroundColor: '#4e54c8',
                                color: 'white',
                                textAlign: 'left'
                            }}>
                                <th style={{ padding: '18px' }}>NOME / CNPJ</th>
                                <th style={{ padding: '18px' }}>CONTATO</th>
                                <th style={{ padding: '18px' }}>LOCALIZAÇÃO</th>
                                <th style={{ padding: '18px' }}>STATUS</th>
                                <th style={{ padding: '18px' }}>AÇÕES</th>
                            </tr>
                        </thead>
                        <tbody>
                            {clientesFiltrados.length === 0 ? (
                                <tr>
                                    <td colSpan="5" style={{
                                        padding: '40px',
                                        textAlign: 'center',
                                        color: '#aaa'
                                    }}>
                                        {busca
                                            ? '🔍 Nenhum cliente encontrado para esta busca'
                                            : '📭 Nenhum cliente cadastrado ainda'}
                                    </td>
                                </tr>
                            ) : (
                                clientesFiltrados.map(c => (
                                    <tr key={c.id} style={{
                                        borderBottom: '1px solid #f0f0f0',
                                        transition: 'background 0.2s'
                                    }}
                                        onMouseEnter={e =>
                                            e.currentTarget.style.backgroundColor = '#f9f9f9'}
                                        onMouseLeave={e =>
                                            e.currentTarget.style.backgroundColor = 'white'}
                                    >
                                        <td style={{ padding: '18px' }}>
                                            <strong>{c.nome}</strong>
                                            <br />
                                            <small style={{ color: '#777' }}>
                                                {maskCNPJ(c.cnpj)}
                                            </small>
                                        </td>
                                        <td style={{ padding: '18px' }}>
                                            {c.email}
                                            <br />
                                            <small style={{ color: '#777' }}>
                                                {maskTelefone(c.telefone)}
                                            </small>
                                        </td>
                                        <td style={{ padding: '18px' }}>
                                            {c.cidade} - {c.estado}
                                        </td>
                                        <td style={{ padding: '18px' }}>
                                            <span style={{
                                                backgroundColor: '#d1fae5',
                                                color: '#065f46',
                                                padding: '6px 12px',
                                                borderRadius: '20px',
                                                fontSize: '12px',
                                                fontWeight: 'bold'
                                            }}>
                                                {c.status}
                                            </span>
                                        </td>
                                        <td style={{ padding: '18px' }}>
                                            <button
                                                onClick={() => prepararEdicao(c)}
                                                style={{
                                                    border: 'none',
                                                    background: 'none',
                                                    cursor: 'pointer',
                                                    marginRight: '15px',
                                                    fontSize: '20px'
                                                }}>✏️</button>
                                            <button
                                                onClick={() => excluirCliente(c.id)}
                                                style={{
                                                    border: 'none',
                                                    background: 'none',
                                                    cursor: 'pointer',
                                                    fontSize: '20px'
                                                }}>🗑️</button>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            </div>

            {/* Modal */}
            {modalAberto && (
                <div style={{
                    position: 'fixed', top: 0, left: 0,
                    width: '100%', height: '100%',
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    display: 'flex', justifyContent: 'center',
                    alignItems: 'center', zIndex: 1000
                }}>
                    <div style={{
                        backgroundColor: 'white',
                        padding: '35px',
                        borderRadius: '15px',
                        width: '480px',
                        boxShadow: '0 10px 30px rgba(0,0,0,0.2)',
                        maxHeight: '90vh',
                        overflowY: 'auto'
                    }}>
                        <h2 style={{
                            marginTop: 0,
                            marginBottom: '20px',
                            textAlign: 'center'
                        }}>
                            {editandoId ? '📝 Editar Cliente' : '👤 Novo Cliente'}
                        </h2>
                        <form onSubmit={salvarCliente} style={{
                            display: 'flex',
                            flexDirection: 'column',
                            gap: '12px'
                        }}>
                            <input name="nome" value={novoCliente.nome}
                                placeholder="Razão Social *" onChange={handleChange}
                                required style={estiloInput} />
                            <input name="cnpj" value={novoCliente.cnpj}
                                placeholder="CNPJ *" onChange={handleChange}
                                required style={estiloInput} />
                            <input name="email" value={novoCliente.email}
                                type="email" placeholder="E-mail"
                                onChange={handleChange} style={estiloInput} />
                            <input name="telefone" value={novoCliente.telefone}
                                placeholder="Telefone" onChange={handleChange}
                                style={estiloInput} />
                            <input name="endereco" value={novoCliente.endereco}
                                placeholder="Endereço" onChange={handleChange}
                                style={estiloInput} />
                            <div style={{ display: 'flex', gap: '10px' }}>
                                <input name="cidade" value={novoCliente.cidade}
                                    placeholder="Cidade" onChange={handleChange}
                                    style={{ ...estiloInput, flex: 3 }} />
                                <input name="estado" value={novoCliente.estado}
                                    placeholder="UF" maxLength="2"
                                    onChange={handleChange}
                                    style={{ ...estiloInput, flex: 1 }} />
                            </div>
                            <div style={{ display: 'flex', gap: '12px', marginTop: '8px' }}>
                                <button type="submit" style={{
                                    flex: 1, padding: '14px',
                                    backgroundColor: '#4e54c8',
                                    color: 'white', border: 'none',
                                    borderRadius: '8px', cursor: 'pointer',
                                    fontWeight: 'bold'
                                }}>
                                    {editandoId ? 'SALVAR' : 'CADASTRAR'}
                                </button>
                                <button type="button"
                                    onClick={() => setModalAberto(false)}
                                    style={{
                                        flex: 1, padding: '14px',
                                        backgroundColor: '#e74c3c',
                                        color: 'white', border: 'none',
                                        borderRadius: '8px', cursor: 'pointer',
                                        fontWeight: 'bold'
                                    }}>
                                    CANCELAR
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

const estiloInput = {
    padding: '12px',
    border: '1px solid #ddd',
    borderRadius: '8px',
    fontSize: '14px',
    outline: 'none',
    width: '100%',
    boxSizing: 'border-box'
};

export default Clientes;