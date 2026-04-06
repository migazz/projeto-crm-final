import React from 'react';
import { useAuth } from '../context/AuthContext';

export default function Header({ toggleSidebar }) {
    const { usuario } = useAuth();

    return (
        <header style={{
            height: '60px',
            width: '100%',
            backgroundColor: '#fff',
            borderBottom: '1px solid #ddd',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            padding: '0 20px',
            position: 'fixed',
            top: 0,
            left: 0,
            zIndex: 900,
            boxSizing: 'border-box'
        }}>
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <button onClick={toggleSidebar} style={{
                    background: 'none', border: 'none',
                    fontSize: '24px', cursor: 'pointer', marginRight: '15px'
                }}>☰</button>
                <h2 style={{ fontSize: '18px', margin: 0, color: '#333' }}>
                    CRM PRO
                </h2>
            </div>

            {usuario && (
                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '10px'
                }}>
                    <div style={{
                        width: '36px', height: '36px',
                        borderRadius: '50%',
                        backgroundColor: '#4e54c8',
                        color: 'white',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontWeight: 'bold',
                        fontSize: '16px'
                    }}>
                        {usuario.nome?.charAt(0).toUpperCase()}
                    </div>
                    <span style={{
                        fontSize: '14px',
                        color: '#555',
                        fontWeight: '500'
                    }}>
                        {usuario.nome}
                    </span>
                </div>
            )}
        </header>
    );
}