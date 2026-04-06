import React, { useState, useEffect } from 'react';
import api from '../services/api';
import './Dashboard.css';

const Dashboard = () => {
    const [totalClientes, setTotalClientes] = useState(0);
    const [oportunidadesAbertas, setOportunidadesAbertas] = useState(0);
    const [vendasConvertidas, setVendasConvertidas] = useState(0);
    const [totalEmNegociacao, setTotalEmNegociacao] = useState(0);
    const [carregando, setCarregando] = useState(true);

    useEffect(() => {
        carregarDados();
    }, []);

    const carregarDados = async () => {
        try {
            const [resClientes, resOportunidades] = await Promise.all([
                api.get('/api/clientes'),
                api.get('/api/oportunidades')
            ]);

            const clientes = resClientes.data;
            const oportunidades = resOportunidades.data;

            // Total de clientes
            setTotalClientes(clientes.length);

            // Oportunidades abertas (tudo que não está fechado)
            const abertas = oportunidades.filter(o =>
                o.estagio !== 'FECHADO_GANHO' && o.estagio !== 'FECHADO_PERDIDO'
            );
            setOportunidadesAbertas(abertas.length);

            // Valor total das vendas convertidas (FECHADO_GANHO)
            const ganhas = oportunidades.filter(o => o.estagio === 'FECHADO_GANHO');
            const totalGanho = ganhas.reduce((acc, o) => acc + (o.valor || 0), 0);
            setVendasConvertidas(totalGanho);

            // Valor total em negociação
            const emNegociacao = oportunidades.filter(o =>
                o.estagio !== 'FECHADO_GANHO' && o.estagio !== 'FECHADO_PERDIDO'
            );
            const totalNegociacao = emNegociacao.reduce((acc, o) => acc + (o.valor || 0), 0);
            setTotalEmNegociacao(totalNegociacao);

        } catch (err) {
            console.error('Erro ao carregar dashboard:', err);
        } finally {
            setCarregando(false);
        }
    };

    const formatarMoeda = (valor) => {
        return valor.toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        });
    };

    if (carregando) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', padding: '60px' }}>
                <p style={{ color: '#888', fontSize: '18px' }}>Carregando dados...</p>
            </div>
        );
    }

    return (
        <div className="dashboard-container">
            <h1>📊 Painel de Controle</h1>
            <p style={{ color: '#888', marginBottom: '30px' }}>
                Visão geral do seu CRM
            </p>

            {/* Cards principais */}
            <div className="dashboard-grid">
                <div className="dashboard-card" style={{ borderTopColor: '#4e54c8' }}>
                    <span className="card-icone">👥</span>
                    <span className="card-titulo">Total de Clientes</span>
                    <span className="dashboard-numero">{totalClientes}</span>
                    <span className="card-subtitulo">clientes cadastrados</span>
                </div>

                <div className="dashboard-card" style={{ borderTopColor: '#f39c12' }}>
                    <span className="card-icone">🔥</span>
                    <span className="card-titulo">Em Negociação</span>
                    <span className="dashboard-numero">{oportunidadesAbertas}</span>
                    <span className="card-subtitulo">oportunidades abertas</span>
                </div>

                <div className="dashboard-card" style={{ borderTopColor: '#2ecc71' }}>
                    <span className="card-icone">✅</span>
                    <span className="card-titulo">Vendas Convertidas</span>
                    <span className="dashboard-numero" style={{ fontSize: '22px' }}>
                        {formatarMoeda(vendasConvertidas)}
                    </span>
                    <span className="card-subtitulo">em negócios fechados</span>
                </div>

                <div className="dashboard-card" style={{ borderTopColor: '#9b59b6' }}>
                    <span className="card-icone">💰</span>
                    <span className="card-titulo">Pipeline Total</span>
                    <span className="dashboard-numero" style={{ fontSize: '22px' }}>
                        {formatarMoeda(totalEmNegociacao)}
                    </span>
                    <span className="card-subtitulo">em oportunidades abertas</span>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;