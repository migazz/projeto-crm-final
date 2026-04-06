import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { toast } from 'react-toastify';
import './Oportunidades.css';

const Oportunidades = () => {
    const [oportunidades, setOportunidades] = useState([]);
    const [clientes, setClientes] = useState([]);
    const [loading, setLoading] = useState(true); // Adicionado para o Skeleton
    const [modalCriarAberto, setModalCriarAberto] = useState(false);
    const [modalDetalhes, setModalDetalhes] = useState(null);
    const [form, setForm] = useState({
        titulo: '',
        valor: 0,
        descricao: '',
        clienteId: '',
        estagio: 'PROSPECCAO'
    });

    const estagios = [
        { id: 'PROSPECCAO', nome: '🔍 Prospecção' },
        { id: 'QUALIFICACAO', nome: '⚖️ Qualificação' },
        { id: 'PROPOSTA', nome: '📄 Proposta' },
        { id: 'NEGOCIACAO', nome: '🤝 Negociação' },
        { id: 'FECHADO_GANHO', nome: '✅ Ganho' }
    ];

    useEffect(() => { carregarDados(); }, []);

    const carregarDados = async () => {
        setLoading(true);
        try {
            const [resOps, resCli] = await Promise.all([
                api.get('/api/oportunidades'),
                api.get('/api/clientes')
            ]);
            setOportunidades(resOps.data);
            setClientes(resCli.data);
        } catch (err) {
            toast.error("Erro ao carregar dados do servidor.");
        } finally {
            // Delay opcional para o skeleton ser visível
            setTimeout(() => setLoading(false), 500);
        }
    };

    const handleValorChange = (e, isManualEdit = false) => {
        let value = e.target.value.replace(/\D/g, "");
        const valorNumerico = value ? (parseFloat(value) / 100) : 0;

        if (isManualEdit) {
            setModalDetalhes({ ...modalDetalhes, valor: valorNumerico });
        } else {
            setForm({ ...form, valor: valorNumerico });
        }
    };

    const alterarEstagio = async (id, estagioAtual, direcao, e) => {
        e.stopPropagation();
        const idx = estagios.findIndex(es => es.id === estagioAtual.toUpperCase());
        const novoIdx = direcao === 'proximo' ? idx + 1 : idx - 1;

        if (novoIdx >= 0 && novoIdx < estagios.length) {
            const opOriginal = oportunidades.find(o => o.id === id);
            try {
                await api.put(`/api/oportunidades/${id}?clienteId=${opOriginal.clienteId}`, {
                    ...opOriginal,
                    estagio: estagios[novoIdx].id
                });
                toast.info(`Movido para: ${estagios[novoIdx].nome}`);
                carregarDados();
            } catch (err) {
                toast.error("Erro ao alterar estágio.");
            }
        }
    };

    const atualizarOportunidade = async () => {
        if (!modalDetalhes.titulo || !modalDetalhes.clienteId) {
            return toast.warning("O título e o cliente são obrigatórios!");
        }

        try {
            await api.put(`/api/oportunidades/${modalDetalhes.id}?clienteId=${modalDetalhes.clienteId}`, modalDetalhes);
            toast.success("Venda atualizada com sucesso!");
            setModalDetalhes(null);
            carregarDados();
        } catch (err) {
            toast.error("Erro ao atualizar oportunidade.");
        }
    };

    const salvar = async (e) => {
        e.preventDefault();

        if (!form.titulo || !form.clienteId) {
            return toast.warning("Preencha o título e selecione um cliente!");
        }

        try {
            const novaOportunidade = { ...form, estagio: 'PROSPECCAO' };
            await api.post('/api/oportunidades', novaOportunidade);
            toast.success("Oportunidade criada no funil!");
            setModalCriarAberto(false);
            carregarDados();
            setForm({
                titulo: '', valor: 0, descricao: '',
                clienteId: '', estagio: 'PROSPECCAO'
            });
        } catch (err) {
            toast.error("Falha ao salvar. Verifique a conexão.");
        }
    };

    const excluirOportunidade = async (id) => {
        if (window.confirm("Deseja realmente excluir esta oportunidade?")) {
            try {
                await api.delete(`/api/oportunidades/${id}`);
                toast.success("Oportunidade removida.");
                setModalDetalhes(null);
                carregarDados();
            } catch (err) {
                toast.error("Erro ao excluir.");
            }
        }
    };

    const getNomeCliente = (clienteId) => {
        return clientes.find(c => c.id === clienteId)?.nome || 'Sem Cliente';
    };

    const getNomeEstagio = (estagioId) => {
        return estagios.find(e => e.id === estagioId)?.nome || estagioId;
    };

    const formatarMoeda = (valor) => {
        if (!valor) return 'R$ 0,00';
        return Number(valor).toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        });
    };

    const getCorEstagio = (estagioId) => {
        const cores = {
            PROSPECCAO: '#6366f1',
            QUALIFICACAO: '#f59e0b',
            PROPOSTA: '#3b82f6',
            NEGOCIACAO: '#8b5cf6',
            FECHADO_GANHO: '#10b981',
            FECHADO_PERDIDO: '#ef4444'
        };
        return cores[estagioId] || '#6366f1';
    };

    return (
        <div className="oportunidades-page">
            <div className="header-secao">
                <h1>💰 Funil de Vendas</h1>
                <button className="btn-nova-op" onClick={() => setModalCriarAberto(true)}>
                    + Nova Oportunidade
                </button>
            </div>

            <div className="kanban-board">
                {estagios.map(col => (
                    <div key={col.id} className="kanban-coluna">
                        <div className="coluna-header">
                            <h3>{col.nome}</h3>
                            <span className="badge-count">
                                {loading ? '...' : oportunidades.filter(o => o.estagio?.toUpperCase() === col.id).length}
                            </span>
                        </div>
                        <div className="cards-container">
                            {loading ? (
                                // Exibe Skeletons enquanto carrega
                                <>
                                    <div className="skeleton"></div>
                                    <div className="skeleton"></div>
                                </>
                            ) : (
                                oportunidades
                                    .filter(o => o.estagio?.toUpperCase() === col.id)
                                    .map(o => (
                                        <div
                                            key={o.id}
                                            className="oportunidade-card"
                                            onClick={() => setModalDetalhes(o)}
                                            style={{ borderLeftColor: getCorEstagio(o.estagio) }}
                                        >
                                            <div className="card-top">
                                                <h4>{o.titulo}</h4>
                                                <p className="card-cliente">👤 {getNomeCliente(o.clienteId)}</p>
                                            </div>
                                            <p className="card-valor">{formatarMoeda(o.valor)}</p>
                                            <div className="card-footer">
                                                <button className={`btn-seta ${o.estagio === 'PROSPECCAO' ? 'hidden' : ''}`}
                                                    onClick={(e) => alterarEstagio(o.id, o.estagio, 'anterior', e)}>❮</button>
                                                <span style={{ fontSize: '11px', color: '#aaa' }}>detalhes</span>
                                                <button className={`btn-seta ${o.estagio === 'FECHADO_GANHO' ? 'hidden' : ''}`}
                                                    onClick={(e) => alterarEstagio(o.id, o.estagio, 'proximo', e)}>❯</button>
                                            </div>
                                        </div>
                                    ))
                            )}
                        </div>
                    </div>
                ))}
            </div>

            {modalDetalhes && (
                <div className="modal-overlay" onClick={() => setModalDetalhes(null)}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2>📝 Editar Oportunidade</h2>
                            <button className="close-x" onClick={() => setModalDetalhes(null)}>×</button>
                        </div>

                        <div className="modal-form">
                            <div className="form-group">
                                <label>Título</label>
                                <input value={modalDetalhes.titulo} onChange={e => setModalDetalhes({ ...modalDetalhes, titulo: e.target.value })} />
                            </div>

                            <div className="form-group">
                                <label>Cliente</label>
                                <select value={modalDetalhes.clienteId} onChange={e => setModalDetalhes({ ...modalDetalhes, clienteId: e.target.value })}>
                                    {clientes.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
                                </select>
                            </div>

                            <div className="form-group">
                                <label>Valor</label>
                                <input
                                    style={{ color: '#10b981', fontWeight: 'bold' }}
                                    value={modalDetalhes.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2, style: 'currency', currency: 'BRL' })}
                                    onChange={e => handleValorChange(e, true)}
                                />
                            </div>

                            <div className="form-group">
                                <label>Descrição</label>
                                <textarea rows="3" value={modalDetalhes.descricao || ''}
                                    onChange={e => setModalDetalhes({ ...modalDetalhes, descricao: e.target.value })} />
                            </div>

                            <div className="modal-acoes">
                                <button onClick={atualizarOportunidade} className="btn-criar">Salvar Alterações</button>
                                <button onClick={() => excluirOportunidade(modalDetalhes.id)} className="btn-cancelar" style={{ backgroundColor: '#fee2e2', color: '#ef4444' }}>Excluir</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {modalCriarAberto && (
                <div className="modal-overlay" onClick={() => setModalCriarAberto(false)}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2>✨ Nova Oportunidade</h2>
                            <button className="close-x" onClick={() => setModalCriarAberto(false)}>×</button>
                        </div>
                        <form onSubmit={salvar} className="modal-form">
                            <div className="form-group">
                                <label>Título da Venda</label>
                                <input required value={form.titulo} onChange={e => setForm({ ...form, titulo: e.target.value })} />
                            </div>
                            <div className="form-group">
                                <label>Valor (R$)</label>
                                <input required value={form.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2, style: 'currency', currency: 'BRL' })} onChange={handleValorChange} />
                            </div>
                            <div className="form-group">
                                <label>Cliente</label>
                                <select required value={form.clienteId} onChange={e => setForm({ ...form, clienteId: e.target.value })}>
                                    <option value="">Selecione o Cliente...</option>
                                    {clientes.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Descrição</label>
                                <textarea rows="3" value={form.descricao} onChange={e => setForm({ ...form, descricao: e.target.value })} />
                            </div>
                            <div className="modal-acoes">
                                <button type="button" className="btn-cancelar" onClick={() => setModalCriarAberto(false)}>Cancelar</button>
                                <button
                                    type="submit"
                                    className="btn-criar"
                                    disabled={!form.titulo || !form.clienteId}
                                    style={{ opacity: (!form.titulo || !form.clienteId) ? 0.5 : 1 }}
                                >
                                    Criar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Oportunidades;