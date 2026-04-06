import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function NaoEncontrado() {
    const navigate = useNavigate();

    return (
        <div style={{
            minHeight: '100vh',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: '#f0f2f5',
            textAlign: 'center',
            padding: '20px'
        }}>
            <div style={{ fontSize: '80px', marginBottom: '20px' }}>🔍</div>
            <h1 style={{
                fontSize: '80px',
                color: '#4e54c8',
                margin: '0',
                fontWeight: 'bold'
            }}>404</h1>
            <h2 style={{ color: '#333', marginTop: '10px' }}>
                Página não encontrada
            </h2>
            <p style={{ color: '#888', marginBottom: '30px', fontSize: '16px' }}>
                A página que você está procurando não existe ou foi movida.
            </p>
            <button
                onClick={() => navigate('/dashboard')}
                style={{
                    padding: '14px 32px',
                    backgroundColor: '#4e54c8',
                    color: 'white',
                    border: 'none',
                    borderRadius: '10px',
                    fontSize: '16px',
                    cursor: 'pointer',
                    fontWeight: 'bold'
                }}>
                🏠 Voltar ao Dashboard
            </button>
        </div>
    );
}